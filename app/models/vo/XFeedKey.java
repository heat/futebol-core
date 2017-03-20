package models.vo;

import javax.persistence.Embeddable;

@Embeddable
public class XFeedKey {

    String key;


    public String get() {
        return key;
    }
}
