package org.yangcentral.yangkit.base;

import org.yangcentral.yangkit.common.api.Namespace;
import org.yangcentral.yangkit.model.api.schema.YangSchemaContext;
import org.yangcentral.yangkit.model.api.stmt.*;
import org.yangcentral.yangkit.model.api.stmt.Module;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * class for yang context, it provides context information for every yang statement
 * @version 1.0.0
 * @author frank feng
 * @since 7/8/2022
 */
public class YangContext {
   private YangSchemaContext schemaContext;
   private Module curModule;
   private Grouping curGrouping;
   private Namespace curNamespace;
   private List<YangContext> mergedContexts = new ArrayList<>();
   private YangStatement self;
   private volatile Map<String, SchemaNode> schemaNodeIdentifierCache;
   private volatile Map<String, Grouping> groupingIdentifierCache;
   private volatile Map<String, Typedef> typedefIdentifierCache;
   private volatile Map<String, Extension> extensionCache;
   private volatile Map<String, Feature> featureCache;
   private volatile Map<String, Identity> identityCache;

   public YangContext(YangSchemaContext schemaContext, Module curModule) {
      this.schemaContext = schemaContext;
      this.curModule = curModule;
   }
   /**
    * construct context from parent
    * @param parent parent context
    * @version 1.0.0
    * @author frank feng
    * @since 7/8/2022
    */

   public YangContext(YangContext parent) {
      this.schemaContext = parent.getSchemaContext();
      this.curModule = parent.getCurModule();
      this.curNamespace = parent.getNamespace();
      this.curGrouping = parent.getCurGrouping();
      this.merge(parent);
   }

   public YangContext clone(){
      YangContext clonedContext = new YangContext(this.getSchemaContext(),this.getCurModule());
      for(YangContext yc: this.mergedContexts){
         clonedContext.mergedContexts.add(yc);
      }
      return clonedContext;
   }

   public YangSchemaContext getSchemaContext() {
      return this.schemaContext;
   }

   public void setSchemaContext(YangSchemaContext schemaContext) {
      this.schemaContext = schemaContext;
   }

   public Module getCurModule() {
      return this.curModule;
   }

   public void setCurModule(Module curModule) {
      this.curModule = curModule;
   }

   public Grouping getCurGrouping() {
      return this.curGrouping;
   }

   public void setCurGrouping(Grouping curGrouping) {
      this.curGrouping = curGrouping;
   }

   public YangSpecification getYangSpecification() {
      YangSpecification yangSpecification = null;
      Module module = this.getCurModule();
      YangVersion yangVersion = module.getYangVersion();
      if (yangVersion != null && !yangVersion.getArgStr().equals("1")) {
         yangSpecification = YangSpecification.getVersion11Spec();
      } else {
         yangSpecification = YangSpecification.getVersion1Spec();
      }

      return yangSpecification;
   }

   public void merge(YangContext other) {
      this.mergedContexts.clear();
      this.mergedContexts.add(other);
   }
/**
 * get typedef from yang context, if not found in local context, it will search from merged contexts.
 * @param name typedef name
 * @version 1.0.0
 * @return org.yangcentral.yangkit.model.api.stmt.Typedef
 * @author frank feng
 * @since 7/8/2022
 */
   public Typedef getTypedef(String name) {
      Map<String, Typedef> cache = this.typedefIdentifierCache;
      if (cache != null && cache.containsKey(name)) {
         return cache.get(name);
      } else {
         Iterator contextIterator = this.mergedContexts.iterator();

         Typedef typedef;
         do {
            if (!contextIterator.hasNext()) {
               return null;
            }

            YangContext mergedContext = (YangContext)contextIterator.next();
            typedef = mergedContext.getTypedef(name);
         } while(typedef == null);

         return typedef;
      }
   }
   /**
    * get grouping from yang context, if not found in local context, it will search from merged contexts.
    * @param name typedef name
    * @version 1.0.0
    * @return org.yangcentral.yangkit.model.api.stmt.Grouping
    * @author frank feng
    * @since 7/8/2022
    */
   public Grouping getGrouping(String name) {
      Map<String, Grouping> cache = this.groupingIdentifierCache;
      if (cache != null && cache.containsKey(name)) {
         return cache.get(name);
      } else {
         Iterator contextIterator = this.mergedContexts.iterator();

         Grouping grouping;
         do {
            if (!contextIterator.hasNext()) {
               return null;
            }

            YangContext mergedContext = (YangContext)contextIterator.next();
            grouping = mergedContext.getGrouping(name);
         } while(grouping == null);

         return grouping;
      }
   }

   public Map<String, SchemaNode> getSchemaNodeIdentifierCache() {
      Map<String, SchemaNode> cache = this.schemaNodeIdentifierCache;
      if (cache == null) {
         synchronized (this) {
            cache = this.schemaNodeIdentifierCache;
            if (cache == null) {
               cache = new ConcurrentHashMap<>();
               this.schemaNodeIdentifierCache = cache;
            }
         }
      }
      return cache;
   }

   public Map<String, Grouping> getGroupingIdentifierCache() {
      Map<String, Grouping> cache = this.groupingIdentifierCache;
      if (cache == null) {
         synchronized (this) {
            cache = this.groupingIdentifierCache;
            if (cache == null) {
               cache = new ConcurrentHashMap<>();
               this.groupingIdentifierCache = cache;
            }
         }
      }
      return cache;
   }

   public Map<String, Typedef> getTypedefIdentifierCache() {
      Map<String, Typedef> cache = this.typedefIdentifierCache;
      if (cache == null) {
         synchronized (this) {
            cache = this.typedefIdentifierCache;
            if (cache == null) {
               cache = new ConcurrentHashMap<>();
               this.typedefIdentifierCache = cache;
            }
         }
      }
      return cache;
   }

   public Map<String, Extension> getExtensionCache() {
      Map<String, Extension> cache = this.extensionCache;
      if (cache == null) {
         synchronized (this) {
            cache = this.extensionCache;
            if (cache == null) {
               cache = new ConcurrentHashMap<>();
               this.extensionCache = cache;
            }
         }
      }
      return cache;
   }

   public Map<String, Feature> getFeatureCache() {
      Map<String, Feature> cache = this.featureCache;
      if (cache == null) {
         synchronized (this) {
            cache = this.featureCache;
            if (cache == null) {
               cache = new ConcurrentHashMap<>();
               this.featureCache = cache;
            }
         }
      }
      return cache;
   }

   public Map<String, Identity> getIdentityCache() {
      Map<String, Identity> cache = this.identityCache;
      if (cache == null) {
         synchronized (this) {
            cache = this.identityCache;
            if (cache == null) {
               cache = new ConcurrentHashMap<>();
               this.identityCache = cache;
            }
         }
      }
      return cache;
   }

   public Namespace getNamespace() {
      return this.curNamespace;
   }

   public void setNamespace(Namespace curNamespace) {
      this.curNamespace = curNamespace;
   }

   public YangStatement getSelf() {
      return this.self;
   }

   public void setSelf(YangStatement self) {
      this.self = self;
   }
}
