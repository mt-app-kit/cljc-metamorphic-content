
(ns metamorphic-content.value
    (:require [dictionary.api            :as dictionary]
              [metamorphic-content.utils :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (metamorphic-content) content-props
  ; {:content (keyword, number or string)(opt)
  ;  :prefix (string)(opt)
  ;   W/ {:content (keyword, number or string)}
  ;  :replacements (numbers or strings in vector)(opt)
  ;   W/ {:content (keyword or string)}
  ;  :suffix (string)(opt)
  ;   W/ {:content (keyword, number or string)}}
  ;
  ; @usage
  ; (value {...})
  ;
  ; @example
  ; (value {:content :first-name})
  ; =>
  ; "First name"
  ;
  ; @example
  ; (value {:content "Hakuna Matata"})
  ; =>
  ; "Hakuna Matata"
  [content-props]
  (letfn [
          ; (string-content {:content "Hi, my name is %!" :replacements ["John"]})
          ; =>
          ; "Hi, my name is John!"
          (string-content [content-props]
            (utils/join-content content-props))

          ; (number-content {:content 42})
          ; =>
          ; "42"
          ;
          ; (number-content {:content 42 :suffix "kg"})
          ; =>
          ; "42kg"
          (number-content [content-props]
            (string-content (update content-props :content str)))

          ; (dictionary-content {:content :apple})
          ; =>
          ; "Apple"
          ;
          ; (dictionary-content {:content :hi-my-name-is-n :replacements ["John"]})
          ; =>
          ; "Hi, my name is John!"
          (dictionary-content [content-props]
            (string-content (update content-props :content dictionary/look-up)))

          ; ...
          (metamorphic-value [{:keys [content] :as content-props}]
            (cond (keyword? content) (dictionary-content content-props)
                  (string?  content) (string-content     content-props)
                  (number?  content) (number-content     content-props)
                  :return   content))]

         ; You can pass the content as a map that contains the :content key and other
         ; parameters or as a shorthand format that will be converted to a map.
         ;
         ; (value {:content "Apple"}) <= Default format
         ; (value "Apple")            <= Shorthand format
         (if (map? content-props)
             (metamorphic-value           content-props)
             (metamorphic-value {:content content-props}))))
