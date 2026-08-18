package org.yangcentral.yangkit.model.api.stmt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

/**
 * Helper holding the "referenced by" statements of a {@link Referencable}.
 *
 * <p>The semantics are identical to the default methods of {@link Referencable}: statements are
 * de-duplicated by <em>identity</em> ({@code ==}) and the insertion order is preserved. The only
 * difference is that an identity based index is maintained alongside the list, which turns the
 * duplicate check from a linear scan into a constant time lookup. This matters a lot for widely
 * referenced statements such as an {@code import} of an extension module, where the number of
 * referencing statements can reach hundreds of thousands and the linear scan degenerates into
 * quadratic behaviour.</p>
 */
public final class ReferencableSupport {
   private final List<YangStatement> referencedBy = new ArrayList<>();
   private final Set<YangStatement> index =
         Collections.newSetFromMap(new IdentityHashMap<YangStatement, Boolean>());

   /**
    * Returns the live list of referencing statements, in insertion order.
    * Callers must not structurally modify it directly; use {@link #addReference} /
    * {@link #delReference} instead, otherwise the identity index goes out of sync.
    */
   public List<YangStatement> getReferencedBy() {
      return this.referencedBy;
   }

   public void addReference(YangStatement yangStatement) {
      if (this.index.add(yangStatement)) {
         this.referencedBy.add(yangStatement);
      }
   }

   public void delReference(YangStatement yangStatement) {
      if (!this.index.remove(yangStatement)) {
         return;
      }
      for (int i = 0; i < this.referencedBy.size(); ++i) {
         if (this.referencedBy.get(i) == yangStatement) {
            this.referencedBy.remove(i);
            return;
         }
      }
   }

   public boolean isReferencedBy(YangStatement yangStatement) {
      return this.index.contains(yangStatement);
   }

   public boolean isReferenced() {
      return !this.referencedBy.isEmpty();
   }

   public void clear() {
      this.referencedBy.clear();
      this.index.clear();
   }
}
