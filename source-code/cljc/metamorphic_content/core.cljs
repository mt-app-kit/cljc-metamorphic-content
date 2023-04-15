
(ns metamorphic-content.core
    (:require [dictionary.api            :as dictionary]
              [hiccup.api                :refer [hiccup?]]
              [metamorphic-content.utils :as utils :refer [component?]]
              [noop.api                  :refer [return]]
              [vector.api                :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn compose
  ; @param (metamorphic-content) content-props
  ; {:content (component, keyword, hiccup, number, string or symbol)(opt)
  ;  :params (vector)(opt)
  ;  :prefix (string)(opt)
  ;   W/ {:content (keyword, number or string)}
  ;  :replacements (numbers or strings in vector)(opt)
  ;   W/ {:content (keyword or string)}
  ;  :suffix (string)(opt)
  ;   W/ {:content (keyword, number or string)}}
  ;
  ; @usage
  ; (compose {...})
  ;
  ; @usage
  ; [compose {...}]
  ;
  ; @example
  ; (compose {:content :first-name})
  ; =>
  ; "First name"
  ;
  ; @example
  ; (compose {:content "Hakuna Matata"})
  ; =>
  ; "Hakuna Matata"
  ;
  ; @usage
  ; (defn my-component [])
  ; [compose :my-content {:content #'my-component}]
  ;
  ; @usage
  ; (defn my-component [my-color])
  ; [compose {:content [my-component :green]}]
  ;
  ; @usage
  ; (defn my-component   [my-color])
  ; (defn your-component [your-color])
  ; [compose {:content [:<> [my-component   :green]
  ;                         [your-component :blue]]}]
  ;
  ; @usage
  ; (defn my-component [color])
  ; [compose :my-content {:content #'my-component
  ;                       :params  [:green]}]
  ;
  ; @usage
  ; (defn my-component [color])
  ; [compose {:content [my-component]
  ;           :params  [:green]}]
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

          ; (hiccup-content {:content [:div "Apple"]})
          ; =>
          ; [:div "Apple"]
          (hiccup-content [content-props]
            (:content content-props))

          ; (defn my-component [] ...)
          ; (render-fn-content {:content my-component})
          ; =>
          ; [my-component]
          ;
          ; (defn my-component [] ...)
          ; (render-fn-content {:content #'my-component})
          ; =>
          ; [my-component]
          ;
          ; (defn my-component [my-param] ...)
          ; (render-fn-content {:content #'my-component :params ["My value"]})
          ; =>
          ; [my-component "My-value"]
          (render-fn-content [{:keys [content params]}]
            (if params (vector/concat-items [content] params)
                       [content]))

          ; (defn my-component [] ...)
          ; (component-content {:content [my-component]})
          ; =>
          ; [my-component]
          ;
          ; (defn my-component [my-param] ...)
          ; (component-content {:content [my-component] :params ["My value"]})
          ; =>
          ; [my-component "My-value"]
          (component-content [{:keys [content params]}]
            (if params (vector/concat-items content params)
                       (return content)))

          ; ...
          (metamorphic-component [{:keys [content] :as content-props}]
            ; #' The symbol must resolve to a var, and the Var object itself (not its value) is returned.
            ;
            ; (var? #'my-component) => true
            ; (fn?  #'my-component) => true
            ; (var?   my-component) => false
            ; (fn?    my-component) => true
            ;
            ; The fn? function matches with both types (#'my-component, my-component).
            (cond (keyword?   content) (dictionary-content content-props)
                  (string?    content) (string-content     content-props)
                  (number?    content) (number-content     content-props)
                ; (var?       content) [render-fn-content  content-props]
                  (fn?        content) [render-fn-content  content-props]
                  (component? content) [component-content  content-props]
                  (hiccup?    content) (hiccup-content     content-props)
                  :return     content))]

         ; You can pass the content as a map that contains the :content key and other
         ; parameters or as a shorthand format that will be converted to a map.
         ;
         ; [component {:content "Apple"}] <= Default format
         ; [component "Apple"]            <= Shorthand format
         (if (map? content-props)
             (metamorphic-component           content-props)
             (metamorphic-component {:content content-props}))))
