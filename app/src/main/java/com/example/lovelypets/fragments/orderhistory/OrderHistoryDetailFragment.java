package com.example.lovelypets.fragments.orderhistory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lovelypets.R;
import com.example.lovelypets.adapters.ProductAdapterForCartFragment;
import com.example.lovelypets.enums.OrderStatus;
import com.example.lovelypets.enums.ProductType;
import com.example.lovelypets.eventlisteners.OnProductClickListener;
import com.example.lovelypets.fragments.productdetail.ProductDetailFragment;
import com.example.lovelypets.models.Order;
import com.example.lovelypets.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment to display the details of a specific order.
 */
public class OrderHistoryDetailFragment extends Fragment implements OnProductClickListener {
    private static final String ARG_ORDER_NUMBER = "order_number";
    private static final String ARG_DATE_OF_CREATED = "date_of_created";
    private static final String ARG_TOTAL_PRICE = "total_price";
    private static final String ARG_ADDRESS_OF_DELIVERY = "address_of_delivery";
    private static final String ARG_ORDER_STATUS = "order_status";
    private static final String ARG_PRODUCTS = "products";

    private Integer orderNumber;
    private String dateOfCreated;
    private Double totalPrice;
    private String addressOfDelivery;
    private OrderStatus orderStatus;
    private final List<Product> productList = new ArrayList<>();
    private RecyclerView productsRecycleView;

    /**
     * Constructor to initialize the product list.
     *
     * @param products List of products in the order.
     */
    public OrderHistoryDetailFragment(List<Product> products) {
        productList.addAll(products);
    }

    /**
     * Factory method to create a new instance of this fragment with order details.
     *
     * @param order The order whose details are to be displayed.
     * @return A new instance of OrderHistoryDetailFragment.
     */
    public static OrderHistoryDetailFragment newInstance(Order order) {
        OrderHistoryDetailFragment fragment = new OrderHistoryDetailFragment(order.getProducts());
        Bundle args = new Bundle();
        args.putInt(ARG_ORDER_NUMBER, order.getOrderNumber());
        args.putString(ARG_DATE_OF_CREATED, order.getDateOfCreated().toString());
        args.putDouble(ARG_TOTAL_PRICE, order.getTotalPrice());
        args.putString(ARG_ADDRESS_OF_DELIVERY, order.getAddressOfDelivery());
        args.putString(ARG_ORDER_STATUS, order.getOrderStatus().toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderNumber = getArguments().getInt(ARG_ORDER_NUMBER);
            dateOfCreated = getArguments().getString(ARG_DATE_OF_CREATED);
            totalPrice = getArguments().getDouble(ARG_TOTAL_PRICE);
            addressOfDelivery = getArguments().getString(ARG_ADDRESS_OF_DELIVERY);
            orderStatus = OrderStatus.valueOf(getArguments().getString(ARG_ORDER_STATUS));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_history_detail, container, false);

        // Initialize views
        productsRecycleView = view.findViewById(R.id.products_view);
        ImageView backImageView = view.findViewById(R.id.back_arrow);
        TextView orderNumberTextView = view.findViewById(R.id.order_number_text);
        TextView dateTextView = view.findViewById(R.id.date_of_order_text);
        TextView totalPriceTextView = view.findViewById(R.id.total_price_text);
        TextView paymentMethodTextView = view.findViewById(R.id.payment_method_text);

        // Set order details to TextViews
        orderNumberTextView.setText("Order №" + orderNumber);
        dateTextView.setText(dateOfCreated);
        totalPriceTextView.setText(totalPrice.intValue() + " KZT");
        paymentMethodTextView.setText("Bank card");

        // Setup RecyclerView for products
        setRecycleView();

        // Set back button click listener
        backImageView.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

    /**
     * Sets up the RecyclerView with a grid layout and attaches the adapter.
     */
    public void setRecycleView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        productsRecycleView.setLayoutManager(gridLayoutManager);
        productsRecycleView.setAdapter(new ProductAdapterForCartFragment(getContext(), productList, this));
    }

    /**
     * Retrieves the resource ID for the given icon name from the mipmap directory.
     *
     * @param iconName The name of the icon.
     * @return The resource ID of the icon.
     */
    private int getMinmapResIdByName(String iconName) {
        String pkgName = requireContext().getPackageName();
        int resId = requireContext().getResources().getIdentifier(iconName, "mipmap", pkgName);
        Log.i("OrderHistoryDetailFragment", "Res Name: " + iconName + " ==> Res Id = " + resId);
        return resId;
    }

    @Override
    public void onProductClicked(Product product) {
        // Navigate to ProductDetailFragment when a product is clicked
        ProductDetailFragment productDetailFragment = ProductDetailFragment.newInstance(
                product.getIconName(), product.getName(), product.getDescription(),
                product.getCategoryId(), product.getPrice(), product.getProductType());

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, productDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
