
(ns metamorphic-content.core
    (:require [dictionary.api            :as dictionary]
              [hiccup.api                :refer [hiccup?]]
              [metamorphic-content.utils :as utils]
              [reagent.api               :as reagent]
              [vector.api                :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn compose
  ; @description
  ; - Designed to create and format content by taking a variety of input types and returning a composed result.
  ; - It allows you to specify content using different formats, such as keywords, strings, numbers, Reagent components,
  ;   or even vectors, and it returns the formatted content.
  ; - Dictionary term keywords are multilingual expression identifiers that can identify
  ;   a specific expression in a specific language in the application dictionary.
  ;   For further information about dictionary term keywords, check out the 'cljc-dictionary' library:
  ;   https://github.com/bithandshake/cljc-dictionary
  ; - You can use Reagent components directly within the content, allowing you to create
  ;   dynamic and interactive content. The components can be specified as symbols or functions (vars).
  ; - You can use the content parameter recursivelly:
  ;   [compose {:content {:content "You can compose recursive structures as contents"}}]
  ;
  ; @param (metamorphic-content) content-props
  ; {:content (Reagent component, dictionary term keyword, hiccup, number, string or symbol of Reagent component)(opt)
  ;  :params (vector)(opt)
  ;  :prefix (string)(opt)
  ;   W/ {:content (keyword, number or string)}
  ;  :replacements (numbers or strings in vector)(opt)
  ;   W/ {:content (keyword or string)}
  ;  :suffix (string)(opt)
  ;   W/ {:content (keyword, number or string)}}
  ; @param (metamorphic-content)(opt) placeholder-props
  ; In case of the composed content is empty the 'compose' function returns the placeholder instead (if any given).
  ;
  ; @usage
  ; (compose {...})
  ;
  ; @usage
  ; [compose {...}]
  ;
  ; @example
  ; It can take a dictionary term identifier as content and return the translated
  ; expression in the selected language.
  ; (compose {:content :first-name})
  ; =>
  ; "First name"
  ;
  ; @example
  ; Raw string contents are rendered as-is, unless at least one of the ':prefix',
  ; ':replacements' or ':suffix' parameters is passed as well.
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
  ([content-props]
   (compose content-props nil))

  ([content-props placeholder-props]
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
                                         (-> content)))

           ; ...
           (metamorphic-content [{:keys [content] :as content-props}]
                                ; #' The symbol must resolve to a var, and the Var object itself (not its value) is returned.
                                ;
                                ; (var? #'my-component) => true
                                ; (fn?  #'my-component) => true
                                ; (var?   my-component) => false
                                ; (fn?    my-component) => true
                                ;
                                ; The fn? function matches both types (#'my-component, my-component),
                                ; therefore no need to apply the 'var?' condition.
                                (cond (keyword?           content) (dictionary-content content-props)
                                      (string?            content) (string-content     content-props)
                                      (number?            content) (number-content     content-props)
                                    ; (var?               content) [render-fn-content  content-props]
                                      (fn?                content) [render-fn-content  content-props]
                                      (reagent/component? content) [component-content  content-props]
                                      (hiccup?            content) (hiccup-content     content-props)
                                      :return             content))

           ; You can pass the content as a map that contains the ':content' key and other
           ; parameters, or in a shorthand format that will be converted to a map.
           ;
           ; [component {:content "Apple"}] <= Default format
           ; [component "Apple"]            <= Shorthand format
           (use-shorthand-f [content-props]
                            (if (-> content-props map?)
                                (-> content-props)
                                {:content content-props}))]

          ; ...
          (let [content-props    (use-shorthand-f     content-props)
                composed-content (metamorphic-content content-props)]
               (if-not (-> composed-content empty?)
                       (-> composed-content)
                       (if placeholder-props (compose placeholder-props)))))))
