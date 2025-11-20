package com.example.shop.product.application;

import com.example.shop.common.ResponseEntity;
import com.example.shop.product.application.dto.ProductCommand;
import com.example.shop.product.application.dto.ProductInfo;
import com.example.shop.product.domain.Product;
import com.example.shop.product.domain.ProductRepository;
import com.example.shop.seller.domain.SellerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    // application 레이어가 product + seller 두개의 도메인을 조합하고 있음
    // product 엔티티는 seller 엔티티를 직접 모름, 단지 sellerId만 가짐
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public ProductService(ProductRepository productRepository,
                          SellerRepository sellerRepository) {
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
    }

    // 상품 목록 조회
    public ResponseEntity<List<ProductInfo>> findAll(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);
        List<ProductInfo> products = page.stream()
                .map(ProductInfo::from)
                .toList();
        return new ResponseEntity<>(HttpStatus.OK.value(), products, page.getTotalElements());
    }

    // 상품 등록
    public ResponseEntity<ProductInfo> create(ProductCommand command) {
        if (command.sellerId() == null) { // 판매자 아이디 필수 체크. 판매자가 없는 상품은 만들 수 없음
            throw new IllegalArgumentException("sellerId is required");
        }
        // 판매자 존재 여부 검증
        sellerRepository.findById(command.sellerId())
                .orElseThrow(() -> new IllegalArgumentException("Seller not found: " + command.sellerId()));
        // operatorId가 따로 넘어오면 그걸 사용, 없으면 sellerId 사용 -> 운영자가 따로 없으면 판매자가 곧 운영자다 (operatorId)
        UUID operator = command.operatorId() != null ? command.operatorId() : command.sellerId();

        // 도메인 생성 호출
        Product product = Product.create(
                command.sellerId(),
                command.name(),
                command.description(),
                command.price(),
                command.stock(),
                command.status(),
                operator
        );
        Product saved = productRepository.save(product);
        return new ResponseEntity<>(HttpStatus.CREATED.value(), ProductInfo.from(saved), 1);
    }

    // 상품 수정
    public ResponseEntity<ProductInfo> update(UUID productId, ProductCommand command) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        // operatorId 없으면 이전 수정자 정보 그대로 씀
        UUID operator = command.operatorId() != null ? command.operatorId() : product.getModifyId();
        product.update(
                command.name(),
                command.description(),
                command.price(),
                command.stock(),
                command.status(),
                operator
        );
        Product updated = productRepository.save(product);
        return new ResponseEntity<>(HttpStatus.OK.value(), ProductInfo.from(updated), 1);
    }

    // 상품 삭제
    public ResponseEntity<Void> delete(UUID productId) {
        productRepository.deleteById(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT.value(), null, 0);
    }
}
