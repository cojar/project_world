package com.example.world.product.specification.macMin;

import com.example.world.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MacMinService {
    public final MacMinRepository macMinRepository;

    public MacMin create(String operatingSystem, String processor, String memory, String graphics, String storage,
                         String directAccess, String network, Product product) {
        MacMin macMin = new MacMin();
        macMin.setOperatingSystem(operatingSystem);
        macMin.setProcessor(processor);
        macMin.setMemory(memory);
        macMin.setGraphics(graphics);
        macMin.setStorage(storage);
        macMin.setDirectAccess(directAccess);
        macMin.setNetwork(network);
        macMin.setProduct(product);
        this.macMinRepository.save(macMin);
        return macMin;
    }

    public List<MacMin> getMacMin() {
        return this.macMinRepository.findAll();
    }
}
