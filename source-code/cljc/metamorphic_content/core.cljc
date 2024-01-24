
(ns metamorphic-content.core
    (:require [app-dictionary.api        :as app-dictionary]
              [fruits.hiccup.api         :refer [hiccup?]]
              [fruits.vector.api         :as vector]
              [metamorphic-content.utils :as utils]
              [reagent.api               :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn compose
  ; @description
  ; - Takes a variety of content types and returns a composed result.
  ; - The provided content (in shorthand form) can be a Reagent component, React form, HICCUP form, number, string or a dictionary term ID.
  ; - The provided content (in longhand form) can be a map that describes the content and contains the actual content as the ':content' property.
  ; - The content can be provided recursivelly:
  ;   (compose {:content {:content "My content"}})
  ;
  ; @param (list of metamorphic-contents) contents
  ; {:content (Reagent component, dictionary term keyword, hiccup, number, string or symbol of Reagent component)(opt)
  ;  :params (vector)(opt)
  ;  :prefix (string)(opt)
  ;  :replacements (numbers or strings in vector)(opt)
  ;  :suffix (string)(opt)}
  ;
  ; @usage
  ; (compose "My content")
  ;
  ; @usage
  ; [compose "My content"]
  ;
  ; @usage
  ; (compose {:content "My content"})
  ;
  ; @usage
  ; [compose {:content "My content"}]
  [contents]
  (letfn [; (string-content {:content "Hi, my name is %!" :replacements ["John"]})
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
                              (string-content (update content-props :content app-dictionary/look-up)))

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
                                        (-> content)))

          ; ...
          (metamorphic-content [{:keys [content] :as content-props}]
                               ; The symbol must resolve to a var, and the Var object itself (not its value) is returned.
                               ;
                               ; (var? #'my-component) => true
                               ; (fn?  #'my-component) => true
                               ; (var?   my-component) => false
                               ; (fn?    my-component) => true
                               ;
                               ; The 'fn?' function matches both types (#'my-component, my-component).
                               ; Therefore, no need to use the 'var?' function as a condition.
                               (cond (keyword?           content) (dictionary-content content-props)
                                     (string?            content) (string-content     content-props)
                                     (number?            content) (number-content     content-props)
                                   ; (var?               content) [render-fn-content  content-props]
                                     (fn?                content) [render-fn-content  content-props]
                                     (reagent/component? content) [component-content  content-props]
                                     (hiccup?            content) (hiccup-content     content-props)
                                     :return             content))
          ; ...
          (compose-content [content-props]
                           (let [content-props    (utils/to-longhand   content-props)
                                 composed-content (metamorphic-content content-props)]
                                (if-not (-> composed-content empty?)
                                        (-> composed-content))))]

         ; ...
         (vector/first-result contents compose-content)))
