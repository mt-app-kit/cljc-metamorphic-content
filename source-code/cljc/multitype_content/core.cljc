
(ns multitype-content.core
    (:require [app-dictionary.api      :as app-dictionary]
              [fruits.hiccup.api       :refer [hiccup?]]
              [fruits.shorthand.api :as shorthand]
              [fruits.mixed.api        :as mixed]
              [fruits.vector.api       :as vector]
              [multitype-content.utils :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn compose
  ; @description
  ; - Takes a variety of content types and returns a composed result.
  ; - The provided content (in shorthand form) can be a Reagent component, React form, HICCUP form, number, string or a dictionary term ID.
  ; - The provided content (in longhand form) can be a map that describes the content and contains the actual content as its ':content' property.
  ;
  ; @param (list of multitype-contents) contents
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
  ;
  ; @usage
  ; ;; The content can be provided recursivelly:
  ; (compose {:content {:content "My content"}})
  ;
  ; @usage
  ; ;; In case the first paramater is composed to an empty value, it composes the second parameter, and so on.
  ; (compose "" nil "My placeholder" ...)
  [& contents]
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
          (multitype-content [{:keys [content] :as content-props}]
                             ; The symbol must resolve to a var, and the Var object itself (not its value) is returned.
                             ;
                             ; (var? #'my-component) => true
                             ; (fn?  #'my-component) => true
                             ; (var?   my-component) => false
                             ; (fn?    my-component) => true
                             ;
                             ; The 'fn?' function matches both types (#'my-component, my-component).
                             ; Therefore, no need to use the 'var?' function as a condition.
                             (cond (keyword?         content) (dictionary-content content-props)
                                   (string?          content) (string-content     content-props)
                                   (number?          content) (number-content     content-props)
                                 ; (var?             content) (render-fn-content  content-props)
                                   (fn?              content) (render-fn-content  content-props)
                                   (utils/component? content) (component-content  content-props)
                                   (hiccup?          content) (hiccup-content     content-props)
                                   :return           content))

          ; ...
          (compose-content [content-props]
                           (let [content-props    (shorthand/apply-shorthand-key content-props :content)
                                 composed-content (multitype-content             content-props)]
                                (if-not (-> composed-content mixed/empty?)
                                        (-> composed-content))))]

         ; ...
         (vector/first-result contents compose-content)))
