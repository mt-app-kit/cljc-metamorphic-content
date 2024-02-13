
(ns metamorphic-content.api
    (:require [metamorphic-content.core :as core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @tutorial Metamorphic content types
;
; @links
; - Check out the [cljc-app-dictionary](https://github.com/mt-app-kit/cljc-app-dictionary) library.
; - Check out the [Reagent](https://github.com/reagent-project/reagent) library.
;
; @---
; Supported content types:
; - Raw string
; - Number
; - Reagent component
; - React from
; - Hiccup form
; - Dictionary term ID

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @tutorial Raw strings
;
; Raw string contents are rendered as-is, unless at least one of the ':prefix', ':replacements' or ':suffix' parameters is provided.
;
; @usage
; (compose "Hakuna Matata")
; =>
; "Hakuna Matata"
;
; @usage
; (compose {:content "420" :prefix "Weight: " :suffix "kg"})
; =>
; "Weight: 420kg"
;
; @usage
; (compose {:content "Hi, my name is %!" :replacements ["John"]})
; =>
; "Hi, my name is John!"
;
; @usage
; (compose {:content "%1 of %2 item(s) downloaded" :replacements [1 5]})
; =>
; "1 of 5 item(s) downloaded"

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @tutorial Numbers
;
; Number contents are converted to strings.
;
; @usage
; (compose {:content 420 :prefix "Weight: " :suffix "kg"})
; =>
; "Weight: 420kg"

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @tutorial Reagent components
;
; Reagent components can be provided as symbols or render vectors.
;
; @usage
; (defn my-component [] [:div ...])
; (defn my-ui
;   []
;   [compose #'my-component])
;
; @usage
; (defn my-component [my-color] [:div ...])
; (defn my-ui
;   []
;   [compose [my-component :green]])
;
;
;
; @---
; Reagent components can take parameters via the 'compose' function.
;
; @usage
; (defn my-component      [my-color]      [:div ...])
; (defn another-component [another-color] [:div ...])
; (defn my-ui
;   []
;   [compose [:<> [my-component      :green]
;                 [another-component :blue]]])
;
; @usage
; (defn my-component [my-color] [:div ...])
; (defn my-ui
;   []
;   [compose {:content #'my-component
;             :params  [:green]}])
;
; @usage
; (defn my-component [my-color] [:div ...])
; (defn my-ui
;   []
;   [compose {:content [my-component]
;             :params  [:green]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @tutorial Dictionary term IDs
;
; Dictionary term IDs are composed to translated dictionary expressions in a selected language.
;
; @usage
; (compose :first-name)
; =>
; "First name"
;
; @usage
; (ns my-namespace
;     (:require [app-dictionary.api :as app-dictionary]
;               [metamorphic-content.api :refer [compose]]))
;
; (app-dictionary/add-term! :apple {:en "Apple" :hu "Alma"})
; (app-dictionary/select-language! :en)
;
; (compose :apple)
; =>
; "Apple"
;
; @usage
; (ns my-namespace
;     (:require [app-dictionary.api :as app-dictionary]
;               [metamorphic-content.api :refer [compose]]))
;
; (app-dictionary/add-term! :hi-my-name-is-n {:en "Hi, my name is %!" :hu "Szia, az én nevem %!"})
; (dictionary/select-language! :en)
;
; (compose {:content :hi-my-name-is-n :replacements ["John"]})
; =>
; "Hi, my name is John!"

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (metamorphic-content.core/*)
(def compose core/compose)
