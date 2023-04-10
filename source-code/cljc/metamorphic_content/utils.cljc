
(ns metamorphic-content.utils
    (:require [string.api :as string]))

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
  ; @example
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
