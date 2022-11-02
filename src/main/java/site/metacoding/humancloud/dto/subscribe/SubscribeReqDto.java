package site.metacoding.humancloud.dto.subscribe;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.humancloud.domain.subscribe.Subscribe;

public class SubscribeReqDto {

    @Getter
    @Setter
    public static class SubscribeSaveReqDto {
        private Integer subscribeUserId;
        private Integer subscribeCompanyId;

        public Subscribe toEntity() {
            return Subscribe.builder()
                    .subscribeCompanyId(this.subscribeCompanyId)
                    .subscribeUserId(this.subscribeUserId)
                    .build();
        }
    }

}
