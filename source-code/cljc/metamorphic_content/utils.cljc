
(ns metamorphic-content.utils
    #?(:clj  (:require [fruits.string.api :as string])
       :cljs (:require [fruits.string.api :as string]
                       [reagent.tools.api :as reagent.tools])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component?
  ; @ignore
  ;
  ; @param (*) n
  ;
  ; @usage
  ; (defn my-component [] [:div ...])
  ; (component? [my-component])
  ; =>
  ; true
  ;
  ; @return (boolean)
  [n]
  #?(:cljs (reagent.tools/component? n)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn join-content
  ; @ignore
  ;
  ; @param (map) content-props
  ; {:content (string)(opt)
  ;  :prefix (string)(opt)
  ;  :replacements (numbers or strings in vector)(opt) replacements
  ;  :suffix (string)(opt)}
  ;
  ; @usage
  ; (join-content {:content "Hi, my name is %!"
  ;                :replacements ["John"]})
  ; =>
  ; "Hi, my name is John!"
  ;
  ; @return (string)
  [{:keys [content prefix replacements suffix]}]
  (if-not (empty? content)
          (if replacements (string/use-replacements (str prefix content suffix) replacements)
                           (str prefix content suffix))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn to-longhand
  ; @ignore
  ;
  ; @param (*) content-props
  ;
  ; @usage
  ; (to-longhand [:div "My-content"])
  ; =>
  ; {:content [:div "My content"]}
  ;
  ; @usage
  ; (to-longhand {:content [:div "My-content"]})
  ; =>
  ; {:content [:div "My content"]}
  ;
  ; @return (map)
  ; {:content (metamorphic-content)}
  [content-props]
  (if (-> content-props map?)
      (-> content-props)
      {:content content-props}))
