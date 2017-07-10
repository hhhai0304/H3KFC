package vn.name.hohoanghai.h3kfc;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import vn.name.hohoanghai.fragment.AboutFragment;
import vn.name.hohoanghai.fragment.DishesFragment;
import vn.name.hohoanghai.fragment.OrderFragment;
import vn.name.hohoanghai.fragment.SettingFragment;
import vn.name.hohoanghai.model.Account;
import vn.name.hohoanghai.util.Utility;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        DishesFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener,OrderFragment.OnFragmentInteractionListener {

    Toolbar toolbar;
    DrawerLayout drawer;

    NavigationView navigationView;
    View headerView;
    ActionBarDrawerToggle toggle;

    ImageView imgUserAvatar;
    TextView txtUser, txtTapToEdit;
    LinearLayout llNavHeader;
    String[] categoriesID, categoriesName;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    int sortMode;
    long back_pressed;
    String category, search;
    boolean isSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
    }

    private void addControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        fragmentManager = getFragmentManager();

        llNavHeader = (LinearLayout) headerView.findViewById(R.id.llNavHeader);
        imgUserAvatar = (ImageView) headerView.findViewById(R.id.imgUserAvatar);
        txtUser = (TextView) headerView.findViewById(R.id.txtUser);
        txtTapToEdit = (TextView) headerView.findViewById(R.id.txtTapToEdit);

        sortMode = 0;
        category = "";
        search = "";
    }

    private void addEvents() {
        llNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSignin) {
                    doSignout();
                } else{
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(createDishFragment(sortMode, category, search));

        getCategories();
    }

    private void doSignout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.signout));
        builder.setPositiveButton(getResources().getString(R.string.btn_signout), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.user_file), MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                finish();
                startActivity(getIntent());
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.btn_cancel), null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setUserAvatar(String url) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_user)
                .showImageOnFail(R.drawable.ic_user)
                .resetViewBeforeLoading(true).cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(1000)).build();
        imageLoader.displayImage(url, imgUserAvatar, options);
    }

    private void getCategories() {

        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.category_file), MODE_PRIVATE);
        int listSize = preferences.getInt("size", 0);
        categoriesID = new String[listSize];
        categoriesID[0] = "";
        for(int i = 1; i < listSize; i++) {
            categoriesID[i] = preferences.getString("categoryID" + i, "");
        }

        categoriesName = new String[listSize];
        categoriesName[0] = getResources().getString(R.string.all);
        for(int i = 1; i < listSize; i++) {
            categoriesName[i] = preferences.getString("name" + i, "");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed + 2000 > System.currentTimeMillis()) {
                System.exit(0);
            } else {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.app_exit), Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility utility = new Utility();
        Account account = utility.getSession(this);
        if(account == null) {
            isSignin = false;
            setUserAvatar("drawable://" + R.drawable.ic_user);
        } else{
            isSignin = true;
            setUserAvatar(account.getAvatar());
            txtUser.setText(account.getName());
            txtTapToEdit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuSearch = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) menuSearch.getActionView();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                search = "";
                replaceFragment(createDishFragment(sortMode, category, search));
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search = query;
                replaceFragment(createDishFragment(sortMode, category, search));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_category:
                createCategoryDialog();
                break;
            case R.id.menu_sort:
                createSortDialog();
                break;
            default: break;
        }

        return super.onOptionsItemSelected(item);

    }

    @SuppressLint("CommitTransaction")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dishes) {
            replaceFragment(createDishFragment(sortMode, category, search));
            setTitle(getResources().getString(R.string.app_name));
        } else if (id == R.id.nav_setting) {
            replaceFragment(new SettingFragment());
            setTitle(getResources().getString(R.string.setting));
        } else if (id == R.id.nav_about) {
            replaceFragment(new AboutFragment());
            setTitle(getResources().getString(R.string.about));
        } else if (id == R.id.nav_orders) {
            replaceFragment(new OrderFragment());
            setTitle(getResources().getString(R.string.order));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private DishesFragment createDishFragment(final int sortMode, final String category, final String search) {
        return new DishesFragment() {
            @Override
            protected int sortMode() {
                return sortMode;
            }

            @Override
            protected String category() {
                return category;
            }

            @Override
            protected String search() {
                return search;
            }
        };
    }

    private void createCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.category));
        builder.setItems(categoriesName, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                category = categoriesID[which];
                replaceFragment(createDishFragment(sortMode, category, search));
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createSortDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.sort));
        builder.setItems(getResources().getStringArray(R.array.sortMenu), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sortMode = which;
                replaceFragment(createDishFragment(sortMode, category, search));
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void replaceFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_fragment, fragment);
        fragmentTransaction.commit();
    }
}