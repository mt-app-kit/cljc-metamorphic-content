
(ns metamorphic-content.api
    (:require [metamorphic-content.core :as core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @tutorial Metamorphic content types
;
; @note
; Check out the [cljc-app-dictionary](https://github.com/mt-app-kit/cljc-app-dictionary) library.
;
; @note
; Check out the [Reagent](https://github.com/reagent-project/reagent) library.
;
;
;
; @title Raw string contents are rendered as-is, unless at least one of the ':prefix', ':replacements' or ':suffix' parameters is provided.
;
; @usage
; (compose "Hakuna Matata")
; =>
; "Hakuna Matata"
;
; @usage
; (resolve {:content "420" :prefix "Weight: " :suffix "kg"})
; =>
; "Weight: 420kg"
;
; @usage
; (resolve {:content "Hi, my name is %!" :replacements ["John"]})
; =>
; "Hi, my name is John!"
;
; @usage
; (resolve {:content "%1 of %2 item(s) downloaded" :replacements [1 5]})
; =>
; "1 of 5 item(s) downloaded"
;
;
;
; @title Number contents are converted to strings.
;
; @usage
; (resolve {:content 420 :prefix "Weight: " :suffix "kg"})
; =>
; "Weight: 420kg"
;
;
;
; @title Reagent components can be provided as symbols or render vectors.
;
; @usage
; (defn my-component [] [:div ...])
; [compose #'my-component]
;
; @usage
; (defn my-component [my-color] [:div ...])
; [compose [my-component :green]]
;
;
;
; @title Reagent components can take parameters via the 'compose' function.
;
; @usage
; (defn my-component      [my-color]      [:div ...])
; (defn another-component [another-color] [:div ...])
; [compose [:<> [my-component      :green]
;               [another-component :blue]]]
;
; @usage
; (defn my-component [my-color] [:div ...])
; [compose {:content #'my-component
;           :params  [:green]}]
;
; @usage
; (defn my-component [my-color] [:div ...])
; [compose {:content [my-component]
;           :params  [:green]}]
;
;
;
; @title Dictionary term IDs are composed to translated dictionary expressions in a selected language.
;
; @usage
; (compose :first-name)
; =>
; "First name"
;
; @usage
; (ns my-namespace
;     (:require [app-dictionary.api :as app-dictionary]
;               [metamorphic-content.api :refer [resolve]]))
;
; (app-dictionary/add-term! :apple {:en "Apple" :hu "Alma"})
; (app-dictionary/select-language! :en)
;
; (resolve :apple)
; =>
; "Apple"
; @---
;
; @usage
; (ns my-namespace
;     (:require [app-dictionary.api :as app-dictionary]
;               [metamorphic-content.api :refer [resolve]]))
;
; (app-dictionary/add-term! :hi-my-name-is-n {:en "Hi, my name is %!" :hu "Szia, az én nevem %!"})
; (dictionary/select-language! :en)
;
; (resolve {:content :hi-my-name-is-n :replacements ["John"]})
; =>
; "Hi, my name is John!"
; @---
;
;
;
; @title In case the first paramater is composed to an empty value, it composes the second parameter, and so on.
;
; @usage
; (compose nil "" "My placeholder" ...)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (metamorphic-content.core/*)
(def compose core/compose)
