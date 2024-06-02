package com.example.lovelypets.fragments.orderhistory;

import android.os.Bundle;

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
import com.example.lovelypets.eventlisteners.OnDeleteIconClickListener;
import com.example.lovelypets.eventlisteners.OnProductClickListener;
import com.example.lovelypets.fragments.productdetail.ProductDetailFragment;
import com.example.lovelypets.models.Order;
import com.example.lovelypets.models.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderHistoryDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
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
    private ImageView backImageView;
    private TextView orderNumberTextView, dateTextView, totalPriceTextView, paymentMethodTextView;
    public OrderHistoryDetailFragment(List<Product> products) {
        productList.addAll(products);
    }

    public static OrderHistoryDetailFragment newInstance(Order order) {
        OrderHistoryDetailFragment fragment = new OrderHistoryDetailFragment(order.getProducts());
        Bundle args = new Bundle();
        args.putInt(ARG_ORDER_NUMBER, order.getOrderNumber());
        args.putString(ARG_DATE_OF_CREATED, order.getDateOfCreated().toString());
        args.putDouble(ARG_TOTAL_PRICE, order.getTotalPrice());
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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_history_detail, container, false);

        productsRecycleView = view.findViewById(R.id.products_view);
        backImageView = view.findViewById(R.id.back_arrow);
        orderNumberTextView = view.findViewById(R.id.order_number_text);
        dateTextView = view.findViewById(R.id.date_of_order_text);
        totalPriceTextView = view.findViewById(R.id.total_price_text);
        paymentMethodTextView = view.findViewById(R.id.payment_method_text);

        orderNumberTextView.setText("Order №" + orderNumber);
        dateTextView.setText(dateOfCreated);
        totalPriceTextView.setText((totalPrice.intValue() + " KZT"));
        paymentMethodTextView.setText("Bank card");
        setRecycleView();
        backImageView.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        return view;
    }

    public void setRecycleView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);  // 2 columns
        productsRecycleView.setLayoutManager(gridLayoutManager);
        productsRecycleView.setAdapter(new ProductAdapterForCartFragment(getContext(), productList, this));
    }
    private int getMinmapResIdByName(String iconName) {
        String pkgName = requireContext().getPackageName();
        int resId = requireContext().getResources().getIdentifier(iconName, "mipmap", pkgName);
        Log.i("CustomListView", "Res Name : " + iconName + "==> Res Id = " + resId);
        return resId;
    }

    @Override
    public void onProductClicked(Product product) {
        ProductDetailFragment productDetailFragment = ProductDetailFragment.newInstance(
                product.getIconName(), product.getName(), product.getDescription(),
                product.getCategoryId(), product.getPrice());

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, productDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}