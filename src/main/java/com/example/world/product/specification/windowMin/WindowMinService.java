package com.example.world.product.specification.windowMin;

import com.example.world.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WindowMinService {
    public final WindowMinRepository windowMinRepository;

    public WindowMin create(String operatingSystem, String processor, String memory, String graphics, String storage,
                            String directAccess, String network, Product product) {
        WindowMin windowMin = new WindowMin();
        windowMin.setOperatingSystem(operatingSystem);
        windowMin.setProcessor(processor);
        windowMin.setMemory(memory);
        windowMin.setGraphics(graphics);
        windowMin.setStorage(storage);
        windowMin.setDirectAccess(directAccess);
        windowMin.setNetwork(network);
        windowMin.setProduct(product);
        this.windowMinRepository.save(windowMin);
        return windowMin;

    }

    public List<WindowMin> getWindowMin() {
        return this.windowMinRepository.findAll();
    }
}
