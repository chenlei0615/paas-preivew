package com.meicloud.ship.preview.core.utils;

import com.meicloud.ship.preview.core.common.ErrorCodeEnum;
import com.meicloud.ship.preview.core.exception.FilePreviewException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * @author chenlei140
 * @className AssertUtils
 * @description 断言
 * @date 2021/12/22 16:55
 */
public class AssertUtils extends Assert {

    public static void isTrue(boolean expression, ErrorCodeEnum errorCodeEnum) {
        if (!expression) {
            throw new FilePreviewException(errorCodeEnum);
        }
    }

    public static void isNull(@Nullable Object object, ErrorCodeEnum errorCodeEnum) {
        if (object != null) {
            throw new FilePreviewException(errorCodeEnum);
        }
    }

    public static void notNull(@Nullable Object object, ErrorCodeEnum errorCodeEnum) {
        if (object == null) {
            throw new FilePreviewException(errorCodeEnum);
        }
    }

    public static void notEmpty(@Nullable Object[] array, ErrorCodeEnum errorCodeEnum) {
        if (ObjectUtils.isEmpty(array)) {
            throw new FilePreviewException(errorCodeEnum);
        }
    }

}
