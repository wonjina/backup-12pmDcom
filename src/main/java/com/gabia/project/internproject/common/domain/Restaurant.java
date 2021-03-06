package com.gabia.project.internproject.common.domain;

import com.gabia.project.internproject.common.helper.PatternType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@TableGenerator(
        name="RESTAURANT_SEQ_GENERATOR",   //자바 내에서 사용할 논리적인 시퀀스 이름, @GeneratedValue의 generator와 매핑
        table = "MY_SEQUENCES",
        pkColumnValue = "RESTAURANT_SEQ",    //필드 중 어떤 필드값과 매팽시킬것인지 지정
        allocationSize = 500                //JPA에서 insert처리 시 마다 키값을 얼마나 증가시킬지 지정
)
public class Restaurant extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "RESTAURANT_SEQ_GENERATOR")
    @Column(name = "restaurant_id")
    private int id;

    @Min(1)
    @Pattern(regexp = "[0-9]*", message = "숫자만 입력해주세요")
    @Column(name = "cell_number")
    private String cellNumber;

    @NotBlank(message = "도로명 주소를 입력해주세요")
    @Column(name = "load_address")
    private String loadAddress;

    @Column(name = "zip_code")
    private String zipCode;

    private String category;

    @Column(name = "location_x")
    private double locationX;

    @Column(name = "location_y")
    private double locationY;

    @Column(name = "review_amount")
    private Long reviewAmount;

    @Column(name = "rating")
    private double rating;

    @NotBlank(message = "가게 이름을 입력해주세요")
    private String name;

    @OneToMany(mappedBy = "restaurant", cascade = { CascadeType.REMOVE, CascadeType.PERSIST }, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Restaurant(String cellNumber,
                      String loadAddress,
                      String zipCode,
                      double locationX,
                      double locationY,
                      String name,
                      Long reviewAmount,
                      double rating) {
        this.cellNumber = cellNumber;
        this.loadAddress = loadAddress;
        this.zipCode = zipCode;
        this.locationX = locationX;
        this.locationY = locationY;
        this.name = name;
        this.reviewAmount = reviewAmount;
        this.rating = rating;
    }
    private Restaurant(int id) {
        this.id = id;
    }

    public static Restaurant of(Integer id) {
        if(id == null) {
            return null;
        }
        return new Restaurant(id);
    }


    /**
     * 연관관계 메소드
     */
    public void addReview(Review review) {
        if (!this.reviews.contains(review)) {
            this.reviews.add(review);
        }
        review.initRestaurant(this);
    }

    public long getReviewsAmount() {
        return reviews.size();
    }

    public double getReviewsRating() {
        if(this.getReviewsAmount() == 0) return 0;

        double  sum = 0;
        for (Review r : reviews) {
            sum += r.getRating();
        }
        return Math.round((sum/this.getReviewsAmount())*10)/10.0;
    }

    public void updateReviewsAmount() {
        this.reviewAmount = (long) reviews.size();
    }

    public void updateRating() {
        this.rating = getReviewsRating();
    }

    public boolean changeCellNumber(String newNumber) {
        if(!(PatternType.isMatchOne(newNumber,"NUM"))) {
            return false;
        }

        this.cellNumber = newNumber;
        return true;
    }

    public boolean changeLoadAddress(String newAddress) {
        if(!(PatternType.nullMatch(newAddress))) {
            return false;
        }

        this.loadAddress = newAddress;
        /*x,y 좌표 변경 로직 추가*/
        return true;
    }

    public boolean changeLocation(int x, int y) {
        this.locationY = y;
        this.locationX = x;
        return true;
    }

    public boolean changeAddress(String newAddress, int x, int y) {
        if(!(PatternType.nullMatch(newAddress))) {
            return false;
        }

        this.locationY = y;
        this.locationX = x;
        this.loadAddress = newAddress;
        return true;
    }

}