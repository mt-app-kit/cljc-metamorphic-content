
(ns multitype-content.utils
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
