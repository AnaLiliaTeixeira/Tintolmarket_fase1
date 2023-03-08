package catalogs;

import java.util.ArrayList;
import java.util.List;

import entities.User;
import entities.Wine;
import entities.WineAd;

public class WineAdCatalog {

	// Private static instance variable of the class
    private static WineAdCatalog instance;
	private List<WineAd> wineAds;
	
	private WineAdCatalog() {
		wineAds = new ArrayList<>();
	}
	
    public static WineAdCatalog getInstance() {
        if (instance == null) {
            instance = new WineAdCatalog();
//            getUsersByTextFile();
        }
        return instance;
    }
    
    public List<WineAd> getWineAdsByUser(User user) {
    	List<WineAd> list = new ArrayList<>();
    	for (WineAd ad : wineAds) {
    		if (ad.getUser().equals(user)) {
    			list.add(ad);
    		}
    	}
    	return list;
    }
    
    public List<WineAd> getWineAdsByWine(Wine wine) {
    	List<WineAd> list = new ArrayList<>();
    	for (WineAd ad : wineAds) {
    		if (ad.getWine().equals(wine)) {
    			list.add(ad);
    		}
    	}
    	return list;
    }
    
    public void add(WineAd wineAd) {
    	this.wineAds.add(wineAd);
    }
    
    public void remove(WineAd wineAd) {
    	this.wineAds.remove(wineAd);
    }
}
