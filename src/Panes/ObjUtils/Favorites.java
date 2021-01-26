package Panes.ObjUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

public class Favorites {
    ArrayList<String[]> favoritesTable = new ArrayList<>();

    public void insert(String title, String url){
        String[] data = {title, url};

        if(!isFavorite(url))
            this.favoritesTable.add(data);
    }

    public boolean isFavorite(String url){
        boolean isFavorite = false;

        for(String[] page: this.favoritesTable){
            if (page[1].equals(url)) {
                isFavorite = true;
                break;
            }
        }

        return isFavorite;
    }

    public ArrayList<String[]> getFavoritesTable() {
        return favoritesTable;
    }

    public int remove(String idName){
        int idx = 0;

        for(int i = 0; i < this.favoritesTable.size(); i++){
            if (this.favoritesTable.get(i)[0].equals(idName)) {
                idx = i;
                this.favoritesTable.remove(i);
                break;
            }
        }

        return idx;
    }

    public String getUrl(String idName){
        String url = "";

        for(int i = 0; i < this.favoritesTable.size(); i++){
            if (this.favoritesTable.get(i)[0].equals(idName)) {
                url = this.favoritesTable.get(i)[1];
                break;
            }
        }

        return url;
    }
}
