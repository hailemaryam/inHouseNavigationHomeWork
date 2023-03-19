package com.example.demo.domain;

import com.example.demo.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MobileStationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MobileStation.class);
        MobileStation mobileStation1 = new MobileStation();
        mobileStation1.setId("id1");
        MobileStation mobileStation2 = new MobileStation();
        mobileStation2.setId(mobileStation1.getId());
        assertThat(mobileStation1).isEqualTo(mobileStation2);
        mobileStation2.setId("id2");
        assertThat(mobileStation1).isNotEqualTo(mobileStation2);
        mobileStation1.setId(null);
        assertThat(mobileStation1).isNotEqualTo(mobileStation2);
    }
}
