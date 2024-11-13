CREATE OR REPLACE FUNCTION apply_bxgy_coupon(
    p_coupon_id INT,
    p_cart JSONB
) RETURNS JSONB AS $$
DECLARE
    coupon_details JSONB;
    buy_products JSONB;
    get_products JSONB;
    repetition_limit INT;
    buy_count INT := 0;
    get_count INT := 0;
    total_discount DECIMAL := 0;
    cart_product JSONB;
    buy_product JSONB;
    get_product JSONB;
    product_id INT;
    quantity INT;
    price DECIMAL;
    cart_quantity INT;
    i INT := 0;
    j INT := 0;
BEGIN
    -- Get coupon details from the database
    SELECT details INTO coupon_details
    FROM coupons
    WHERE coupon_id = p_coupon_id;

    -- Extract buy_products, get_products, and repetition_limit
    buy_products := coupon_details->'buy_products';
    get_products := coupon_details->'get_products';
    repetition_limit := (coupon_details->>'repition_limit')::INT;

    -- Debug: Log the coupon details and cart to verify input
    RAISE NOTICE 'Coupon Details: %', coupon_details;
    RAISE NOTICE 'Cart Data: %', p_cart;

    -- Loop through the buy_products and calculate the count from cart
    FOR i IN 0..jsonb_array_length(buy_products) - 1 LOOP
        buy_product := buy_products->i;
        product_id := (buy_product->>'product_id')::INT;
        quantity := (buy_product->>'quantity')::INT;

        -- Get the quantity of the product from the cart
        cart_quantity := 0;
        FOR cart_product IN SELECT * FROM jsonb_array_elements(p_cart) AS cart_item LOOP
            IF (cart_product->>'product_id')::INT = product_id THEN
                cart_quantity := cart_quantity + (cart_product->>'quantity')::INT;
            END IF;
        END LOOP;

        -- If cart has enough quantity of the "buy" product, increase buy_count
        IF cart_quantity >= quantity THEN
            buy_count := buy_count + LEAST(cart_quantity / quantity, repetition_limit);
        END IF;

        -- Debug: Log the cart quantities and calculated buy count
        RAISE NOTICE 'Buy Product: %, Cart Quantity: %, Buy Count: %', product_id, cart_quantity, buy_count;
    END LOOP;

    -- Debug: Log the buy_count after the loop
    RAISE NOTICE 'Final Buy Count: %', buy_count;

    -- Loop through the get_products and calculate the possible free products
    FOR j IN 0..jsonb_array_length(get_products) - 1 LOOP
        get_product := get_products->j;
        product_id := (get_product->>'product_id')::INT;
        quantity := (get_product->>'quantity')::INT;

        -- Get the price of the get product from the cart
        price := 0;
        FOR cart_product IN SELECT * FROM jsonb_array_elements(p_cart) AS cart_item LOOP
            IF (cart_product->>'product_id')::INT = product_id THEN
                price := (cart_product->>'price')::DECIMAL;
            END IF;
        END LOOP;

        -- If the product was found in the cart and has a price, calculate the discount
        IF price > 0 THEN
            -- Calculate how many times the "get" products can be applied based on the buy_count
            get_count := LEAST(buy_count, repetition_limit);
            -- Add to the total discount (each "get" product is discounted by its price * quantity)
            total_discount := total_discount + (price * get_count * quantity);
            RAISE NOTICE 'Adding discount for product_id: %, count: %, total_discount: %', product_id, get_count, total_discount;
        END IF;
    END LOOP;

    -- Debug: Log the final discount calculation
    RAISE NOTICE 'Final Discount: %', total_discount;

    -- Return the result as JSON
    RETURN jsonb_build_object(
        'coupon_id', p_coupon_id,
        'type', 'bxgy',
        'discount', total_discount
    );
END;
$$ LANGUAGE plpgsql;