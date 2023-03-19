package com.example.demo.domain;

import com.example.demo.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BaseStationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BaseStation.class);
        BaseStation baseStation1 = new BaseStation();
        baseStation1.setId("id1");
        BaseStation baseStation2 = new BaseStation();
        baseStation2.setId(baseStation1.getId());
        assertThat(baseStation1).isEqualTo(baseStation2);
        baseStation2.setId("id2");
        assertThat(baseStation1).isNotEqualTo(baseStation2);
        baseStation1.setId(null);
        assertThat(baseStation1).isNotEqualTo(baseStation2);
    }
}
