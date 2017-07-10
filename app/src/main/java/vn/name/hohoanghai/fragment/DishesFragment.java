package vn.name.hohoanghai.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import vn.name.hohoanghai.adapter.DishAdapter;
import vn.name.hohoanghai.h3kfc.CartViewActivity;
import vn.name.hohoanghai.h3kfc.R;
import vn.name.hohoanghai.model.Product;
import vn.name.hohoanghai.util.DatabaseHelper;

public abstract class DishesFragment extends Fragment {

    RecyclerView rvDishes;
    ArrayList<Product> products;
    DishAdapter adapter;
    DatabaseHelper databaseHelper;
    TextView txtNoResults;
    FloatingActionButton fab;

    protected abstract int sortMode();
    protected abstract String category();
    protected abstract String search();

    public DishesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dishes, container, false);

        txtNoResults = (TextView) view.findViewById(R.id.txtNoResults);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        databaseHelper = new DatabaseHelper(getActivity());
        products = databaseHelper.getProductList(sortMode(), category(), search());

        if(products == null) {
            txtNoResults.setVisibility(View.VISIBLE);
        } else {
            adapter = new DishAdapter(getActivity(), products);

            rvDishes = (RecyclerView) view.findViewById(R.id.rvDishes);
            rvDishes.setHasFixedSize(true);
            rvDishes.addItemDecoration(new GridSpacingItemDecoration(2, 50, true));
            rvDishes.setLayoutManager(new GridLayoutManager((getActivity()),2));
            rvDishes.setAdapter(adapter);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CartViewActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnFragmentInteractionListener)) {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
    }

    class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}