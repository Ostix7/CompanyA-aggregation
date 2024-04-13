package company.a.charlee.entity.generic;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class SentimentValuedEntity {

    @Column(name = "sentiment_value")
    private Double sentimentValue;

}
