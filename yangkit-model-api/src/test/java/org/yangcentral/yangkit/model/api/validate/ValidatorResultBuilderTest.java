package org.yangcentral.yangkit.model.api.validate;

import org.junit.jupiter.api.Test;
import org.yangcentral.yangkit.common.api.validate.ValidatorResult;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidatorResultBuilderTest {
   @Test
   void sharesImmutableEmptyResult() {
      ValidatorResult first = new ValidatorResultBuilder().build();
      ValidatorResult second = new ValidatorResultBuilder().build();

      assertSame(first, second);
      assertTrue(first.isOk());
      assertNull(first.getRecords());

      first.clear();
      assertTrue(second.isOk());
      assertNull(second.getRecords());
   }
}
