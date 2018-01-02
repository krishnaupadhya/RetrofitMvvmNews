package com.urban.piper.home.viewmodel;

import android.databinding.ObservableField;
import android.view.View;

import com.urban.piper.app.DatabaseController;
import com.urban.piper.common.viewmodel.BaseViewModel;
import com.urban.piper.home.listener.FoodListListener;
import com.urban.piper.model.FoodInfo;
import com.urban.piper.utility.LogUtility;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class FoodListActivityViewModel extends BaseViewModel {

    private String TAG = FoodListActivityViewModel.class.getSimpleName();
    private final FoodListListener foodListListener;
    public ObservableField<Boolean> isProgressRingVisible;
    public ObservableField<Boolean> isFoodListListVisible;
    public ObservableField<String> totalPrice;
    public ObservableField<String> subTotal;
    public ObservableField<String> deliveryCharges;

    public FoodListActivityViewModel(FoodListListener homeListener) {
        this.isProgressRingVisible = new ObservableField<>(false);
        this.isFoodListListVisible = new ObservableField<>(false);
        this.totalPrice = new ObservableField<>("₹ 0.0");
        this.subTotal = new ObservableField<>("₹ 0.0");
        this.deliveryCharges = new ObservableField<>("₹ 0.0");
        this.foodListListener = homeListener;
    }

    public void setSubTotal(String subTotal) {
        if (this.subTotal == null)
            this.subTotal = new ObservableField<>("₹ 0.0");
        this.subTotal.set(subTotal);
    }

    public void setTotalPrice(String totalPrice) {
        if (this.totalPrice == null)
            this.totalPrice = new ObservableField<>("₹ 0.0");
        this.totalPrice.set(totalPrice);
    }

    public void setDeliveryCharges(String deliveryCharges) {
        if (this.deliveryCharges == null)
            this.deliveryCharges = new ObservableField<>("₹ 0.0");
        this.deliveryCharges.set(deliveryCharges);
    }

    public void setIsFoodListListVisible(Boolean isFoodListListVisible) {
        this.isFoodListListVisible.set(isFoodListListVisible);
    }

    public void setIsProgressRingVisible(Boolean isProgressRingVisible) {
        this.isProgressRingVisible.set(isProgressRingVisible);
    }

    public void onProceedClick(View view) {
        foodListListener.onProceedToCheckoutClick();
    }

    public void onPlaceOrderClick(View view) {
        resetFoodItems();
    }

    public ArrayList<FoodInfo> fetchCachedData() {
        ArrayList artliclesList = new ArrayList();
        RealmResults<FoodInfo> cachedArticles = DatabaseController.getInstance().getArticlesFromDb();
        if (cachedArticles != null && cachedArticles.size() > 0) {
            artliclesList.addAll(cachedArticles);
            return artliclesList;
        } else {
            FoodInfo info = new FoodInfo();
            info.setTitle("Schezwan Fried Rice");
            info.setImageUrl("https://i.ytimg.com/vi/OUhyJPJlfS8/maxresdefault.jpg");
            info.setPrice(105);
            info.setQuantity(0);
            info.setNonVeg(false);
            info.setArticleId("1");
            artliclesList.add(info);
            saveToRealmDb(info);

            info = new FoodInfo();
            info.setTitle("Spatchcock Teriyaki Chicken with Quinoa Brown Rice");
            info.setImageUrl("https://www.holleygrainger.com/wp-content/uploads/2016/10/One-Pan-Spatchcock-Chicken-and-Veggies-22.jpg");
            info.setPrice(230);
            info.setQuantity(0);
            info.setNonVeg(true);
            info.setArticleId("2");
            artliclesList.add(info);
            saveToRealmDb(info);

            info = new FoodInfo();
            info.setTitle("Jaipuri Kofta");
            info.setImageUrl("http://www.11flowers.in/restaurant/wp-content/uploads/2017/11/jaipuri-veg-kofta-300x300.jpg");
            info.setPrice(110);
            info.setQuantity(0);
            info.setNonVeg(false);
            info.setArticleId("3");
            artliclesList.add(info);
            saveToRealmDb(info);

            info = new FoodInfo();
            info.setTitle("Cheesy Cajun Chicken Burger");
            info.setImageUrl("https://www.bbcgoodfood.com/sites/default/files/styles/carousel_small/public/recipe_images/cajun.jpg?itok=sYUO0-bd");
            info.setPrice(105);
            info.setQuantity(0);
            info.setArticleId("4");
            info.setNonVeg(false);
            artliclesList.add(info);
            saveToRealmDb(info);

            info = new FoodInfo();
            info.setTitle("Schezwan Fried Rice");
            info.setImageUrl("https://i.ytimg.com/vi/OUhyJPJlfS8/maxresdefault.jpg");
            info.setPrice(105);
            info.setQuantity(0);
            info.setNonVeg(false);
            info.setArticleId("5");
            artliclesList.add(info);
            saveToRealmDb(info);

            info = new FoodInfo();
            info.setTitle("Spatchcock Teriyaki Chicken with Quinoa Brown Rice");
            info.setImageUrl("https://www.holleygrainger.com/wp-content/uploads/2016/10/One-Pan-Spatchcock-Chicken-and-Veggies-22.jpg");
            info.setPrice(230);
            info.setQuantity(0);
            info.setNonVeg(true);
            info.setArticleId("6");
            artliclesList.add(info);
            saveToRealmDb(info);

            info = new FoodInfo();
            info.setTitle("Jaipuri Kofta");
            info.setImageUrl("http://www.11flowers.in/restaurant/wp-content/uploads/2017/11/jaipuri-veg-kofta-300x300.jpg");
            info.setPrice(110);
            info.setQuantity(0);
            info.setNonVeg(false);
            info.setArticleId("7");
            artliclesList.add(info);
            saveToRealmDb(info);

            info = new FoodInfo();
            info.setTitle("Cheesy Cajun Chicken Burger");
            info.setImageUrl("https://www.bbcgoodfood.com/sites/default/files/styles/carousel_small/public/recipe_images/cajun.jpg?itok=sYUO0-bd");
            info.setPrice(105);
            info.setQuantity(0);
            info.setNonVeg(false);
            info.setArticleId("8");
            artliclesList.add(info);
            saveToRealmDb(info);
            return artliclesList;
        }
    }

    private void saveToRealmDb(FoodInfo articles) {
        //save  the response into realm database
        DatabaseController.getInstance().getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    realm.copyToRealmOrUpdate(articles);

                } catch (Exception e) {
                    LogUtility.e(TAG, "Error getting news ");
                }
            }
        });
    }

    public void setPriceDetails() {
        RealmResults<FoodInfo> priceList = DatabaseController.getInstance().getArticleByCheckout();
        if (priceList == null || priceList.size() == 0) {

            setSubTotal("₹0.0");
            setDeliveryCharges("₹0.0");
            setTotalPrice("₹0.0");
            return;
        }
        double price = 0.0;
        for (FoodInfo info : priceList
                ) {
            price = price + (info.getQuantity() * info.getPrice());
        }
        double total = price + 40;
        subTotal.set("₹" + price);
        totalPrice.set("₹" + total);
        setDeliveryCharges("₹ 40");
    }

    public void onQtyChangedClick(FoodInfo foodItem, int position, boolean isAddQty) {
        DatabaseController.getInstance().getRealm().executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
            @Override
            public void execute(Realm realm) {
                // increment index
                if (isAddQty) {
                    foodItem.setQuantity(foodItem.getQuantity() + 1);
                    setPriceDetails();
                } else if (foodItem.getQuantity() > 0) {
                    foodItem.setQuantity(foodItem.getQuantity() - 1);
                }
                DatabaseController.getInstance().saveRealmObject(foodItem);

            }
        });
    }

    public void resetFoodItems() {
        DatabaseController.getInstance().getRealm().executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
            @Override
            public void execute(Realm realm) {

                ArrayList<FoodInfo> foodList = fetchCachedData();
                if (foodList == null || foodList.size() == 0) return;
                for (FoodInfo info : foodList
                        ) {
                    info.setQuantity(0);
                }
            }
        });
        setPriceDetails();
        foodListListener.onResetSuccess();

    }


}
