package com.example.dingu.axicut.Inward;

/**
 * Created by dingu on 25/7/17.
 */

public interface SaleOrderNumsFetcher {
    void fetchSaleOrderNumbersFromDatabase(Long startTS,Long endTS,int limit);
}
