package com.example.world.product.specification.windowRecommended;

import com.example.world.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WindowRecommendedService {
    public final WindowRecommendedRepository windowRecommendedRepository;

    public WindowRecommended create(String operatingSystem, String processor, String memory, String graphics, String storage,
                                    String directAccess, String network, Product product) {
        WindowRecommended windowRecommended = new WindowRecommended();
        windowRecommended.setOperatingSystem(operatingSystem);
        windowRecommended.setProcessor(processor);
        windowRecommended.setMemory(memory);
        windowRecommended.setGraphics(graphics);
        windowRecommended.setStorage(storage);
        windowRecommended.setDirectAccess(directAccess);
        windowRecommended.setNetwork(network);
        windowRecommended.setProduct(product);
        this.windowRecommendedRepository.save(windowRecommended);
        return windowRecommended;
    }

    public List<WindowRecommended> getWindowRecommended() {
        return this.windowRecommendedRepository.findAll();
    }
}
