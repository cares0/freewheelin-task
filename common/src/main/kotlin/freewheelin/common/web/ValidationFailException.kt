package freewheelin.common.web

import org.springframework.util.LinkedMultiValueMap

class ValidationFailException(val fieldAndMessageMap: LinkedMultiValueMap<String, String>) :
    IllegalArgumentException("[요청 데이터 검증 실패] \n $fieldAndMessageMap") {
}