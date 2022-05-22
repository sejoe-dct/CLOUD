package com.cloud.web.repository;

import com.cloud.web.domain.FoodBoard;
import com.cloud.web.dto.request.FoodBoardCondition;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.cloud.web.domain.QFoodBoard.foodBoard;

/**
 *  실제 다중 검색 기능을 수행하는 메소드를 정의하는 Impl
 */
public class FoodBoardRepositoryImpl implements FoodBoardRepositoryCustom {

    // 쿼리 dsl를 사용하기 위해서 factory 필요하다.
    private final JPAQueryFactory queryFactory;

    public FoodBoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    /**
     * 쿼리 dsl를 활용하여 동적쿼리 구성 (다중 검색 기능)
     *
     * import static com.cloud.web.domain.QFoodBoard.foodBoard
     */
    @Override
    public List<FoodBoard> search(FoodBoardCondition condition) {

        return queryFactory
                .select(foodBoard)
                .from(foodBoard)
                .where(titleLike(condition.getTitle()),
                        locationTypeEq(condition.getLocationType_Id()),
                        foodTypeEq(condition.getFoodType_id()))
                .fetch();
    }


    private BooleanExpression titleLike(String title) {
        return title.isEmpty() ? null : foodBoard.title.contains(title);
    }
    private BooleanExpression locationTypeEq(Long locationType_Id) {
        return locationType_Id == null ? null : foodBoard.locationType.id.eq(locationType_Id);
    }
    private BooleanExpression foodTypeEq(Long foodType_Id) {
        return foodType_Id == null ? null : foodBoard.foodType.id.eq(foodType_Id);
    }


    /**
     * 페이징 처리도 함께하는 다중 검색 기능
     */
    @Override
    public Page<FoodBoard> searchPageSimple(FoodBoardCondition condition, Pageable pageable) {

        QueryResults<FoodBoard> results = queryFactory
                .select(foodBoard)
                .from(foodBoard)
                .where(titleLike(condition.getTitle()),
                        locationTypeEq(condition.getLocationType_Id()),
                        foodTypeEq(condition.getFoodType_id()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<FoodBoard> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable , total);

    }

}
