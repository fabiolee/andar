package edu.dhbw.andobjviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.dhbw.andarmodelviewer.R;
import edu.dhbw.andobjviewer.models.Product;
import edu.dhbw.andobjviewer.securecheckout.SecureCheckoutActivity;

/**
 * Created by Edwin on 23/11/2016.
 */

public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private CartAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar mToolbar;
    private FrameLayout mBtnCheckout;

    private List<Product> mList = new ArrayList<>();
    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mRecyclerView = (RecyclerView) findViewById(R.id.cartView);

        mBtnCheckout = (FrameLayout) findViewById(R.id.btn_checkout);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.label_cart);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(mToolbar);

        mBtnCheckout.setOnClickListener(this);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mAdapter = new CartAdapter(mList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mProduct = new Product("Sofa", 350, R.drawable.sofa);
        mList.add(mProduct);

        mProduct = new Product("Table", 400, R.drawable.table);
        mList.add(mProduct);

        mProduct = new Product("Chair", 100, R.drawable.chair);
        mList.add(mProduct);

        mProduct = new Product("Cupboard", 500, R.drawable.cupboard);
        mList.add(mProduct);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_checkout:
                startActivity(new Intent(CartActivity.this, SecureCheckoutActivity.class));
                break;
        }
    }

    class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
        private List<Product> mList;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTitle;
            TextView mPrice;
            ImageButton mFavourite;
            ImageButton mThrash;
            ImageButton mPlus;
            ImageButton mMinus;
            TextView mValue;
            ImageView mIcon;

            ViewHolder(View v) {
                super(v);
                mTitle = (TextView) v.findViewById(R.id.product_title);
                mPrice = (TextView) v.findViewById(R.id.product_price);
                mFavourite = (ImageButton) v.findViewById(R.id.favourite);
                mThrash = (ImageButton) v.findViewById(R.id.trash);
                mPlus = (ImageButton) v.findViewById(R.id.plus);
                mMinus = (ImageButton) v.findViewById(R.id.minus);
                mValue = (TextView) v.findViewById(R.id.value);
                mIcon = (ImageView) v.findViewById(R.id.product_img);
            }
        }

        CartAdapter(List<Product> list) {
            this.mList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activty_cart_list, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final CartAdapter.ViewHolder holder, final int position) {
            final int[] counter = {1};

            final Product pos = mList.get(position);
            holder.mTitle.setText(pos.getTitle());
            holder.mPrice.setText(Integer.toString(pos.getPrice()));
            holder.mIcon.setImageResource(pos.getImage());

            holder.mFavourite.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    holder.mFavourite.setImageResource(R.drawable.ic_favorite_red_24dp);
                }
            });

            holder.mThrash.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    mList.remove(pos);
                    notifyItemRemoved(position);
                }
            });

            holder.mMinus.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    if (!(counter[0] < 1)) {
                        counter[0]--;
                        holder.mValue.setText(String.valueOf(counter[0]));
                    }

                    if (counter[0] == 0) {
                        mList.remove(pos);
                        notifyItemRemoved(position);
                    }
                }
            });

            holder.mPlus.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    counter[0]++;
                    holder.mValue.setText(String.valueOf(counter[0]));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
