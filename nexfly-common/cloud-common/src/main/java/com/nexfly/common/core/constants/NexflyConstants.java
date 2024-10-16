package com.nexfly.common.core.constants;

public interface NexflyConstants {

    String API_PREFIX_MAPPING = "/api";

    String USER_ID = "userId";

    enum Status {
        /**
         * 本地上传
         */
        NORMAL(1),

        /**
         * 网络爬取
         */
        DELETE(0);

        public final Integer value;

        Status(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return this.value;
        }
    }

}
