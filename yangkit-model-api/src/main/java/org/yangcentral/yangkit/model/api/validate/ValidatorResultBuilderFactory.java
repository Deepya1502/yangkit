package org.yangcentral.yangkit.model.api.validate;

import org.yangcentral.yangkit.common.api.Builder;
import org.yangcentral.yangkit.common.api.BuilderFactory;
import org.yangcentral.yangkit.common.api.validate.ValidatorResult;

public class ValidatorResultBuilderFactory implements BuilderFactory<ValidatorResult> {
   @Override
   public Builder<ValidatorResult> getBuilder() {
      return new ValidatorResultBuilder();
   }
}
