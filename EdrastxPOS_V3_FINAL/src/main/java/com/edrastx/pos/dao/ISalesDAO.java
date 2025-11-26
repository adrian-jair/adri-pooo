package com.edrastx.pos.dao;

import com.edrastx.pos.model.Sale;
import com.edrastx.pos.model.SaleItem;

import java.time.LocalDateTime;
import java.util.List;

public interface ISalesDAO {
    int saveSale(Sale sale, List<SaleItem> items);
    List<Sale> findByDateRange(LocalDateTime from, LocalDateTime to);
}
