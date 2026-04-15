package com.sb2.service;

import com.sb2.dto.Item;
import com.sb2.entity.ExtractedData;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CpParserMock {

    public ExtractedData parse(String fileName) {

        List<Item> items = new ArrayList<>();

        items.add(createItem("Ноутбук Lenovo", BigDecimal.valueOf(85000), "ООО Техно"));
        items.add(createItem("Ноутбук HP", BigDecimal.valueOf(87000), "ООО КомпСнаб"));
        items.add(createItem("Ноутбук Asus", BigDecimal.valueOf(82000), "ООО Диджитал"));
        items.add(createItem("Ноутбук Acer", BigDecimal.valueOf(83000), "ООО ТехМаркет"));
        items.add(createItem("Ноутбук Dell", BigDecimal.valueOf(91000), "ООО Поставка"));
        items.add(createItem("Ноутбук MSI", BigDecimal.valueOf(88000), "ООО ГигаТех"));

        ExtractedData data = new ExtractedData();
        data.setItems(items);

        return data;
    }

    private Item createItem(String name, BigDecimal price, String supplier) {
        return new Item(name, price, supplier);
    }
}
