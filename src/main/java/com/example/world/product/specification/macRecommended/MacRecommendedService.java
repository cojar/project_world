package com.example.world.product.specification.macRecommended;

import com.example.world.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MacRecommendedService {
    public final MacRecommendedRepository macRecommendedRepository;

    public MacRecommended create(String operatingSystem, String processor, String memory, String graphics, String storage,
                                 String directAccess, String network, Product product) {
        MacRecommended macRecommended = new MacRecommended();
        macRecommended.setOperatingSystem(operatingSystem);
        macRecommended.setProcessor(processor);
        macRecommended.setMemory(memory);
        macRecommended.setGraphics(graphics);
        macRecommended.setStorage(storage);
        macRecommended.setDirectAccess(directAccess);
        macRecommended.setNetwork(network);
        macRecommended.setProduct(product);
        this.macRecommendedRepository.save(macRecommended);
        return macRecommended;
    }

    public List<MacRecommended> getMacRecommended() {
        return this.macRecommendedRepository.findAll();
    }
}
