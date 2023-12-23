
# cljc-metamorphic-content

### Overview

The <strong>cljc-metamorphic-content</strong> is a simple multi-type content renderer for Clojure projects.
It can compose different types of contents such as Reagent components, React forms, Hiccup structures,
numbers, strings or multilingual terms.

> UI components in this library are [Reagent](https://github.com/reagent-project/reagent) components.

### deps.edn

```
{:deps {bithandshake/cljc-metamorphic-content {:git/url "https://github.com/bithandshake/cljc-metamorphic-content"
                                               :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}}}
```

### Current version

Check out the latest commit on the [release branch](https://github.com/bithandshake/cljc-metamorphic-content/tree/release).

### Documentation

The <strong>cljc-metamorphic-content</strong> functional documentation is [available here](https://bithandshake.github.io/cljc-metamorphic-content).

### Changelog

You can track the changes of the <strong>cljc-metamorphic-content</strong> library [here](CHANGES.md).

# Usage

> Some parameters of the following functions and some further functions are not discussed in this file.
  To learn more about the available functionality, check out the [functional documentation](documentation/COVER.md)!

### Index

- [Metamorphic content as a function](#metamorphic-content-as-a-function)

- [Metamorphic content as a Reagent component](#metamorphic-content-as-a-reagent-component)

### Metamorphic content as a function

The [`metamorphic-content.api/resolve`](documentation/cljc/metamorphic-content/API.md#resolve)
function takes a map (or its shorthand form) that describes the content what the function returns.

If the content has no additional properties the shorthand form could be used as well.

```
(resolve {:content "Apple"})
; =>
; "Apple"

(resolve "Apple")
; =>
; "Apple"
```

If the content is a string you can pass the `:prefix` and `:suffix` properties which
will be joined to the content and/or the `:replacements` property (as a vector that
contains replacement values).

```
(resolve {:content "Apple"})
; =>
; "Apple"
```

```
(resolve {:content "420" :prefix "Weight: " :suffix "kg"})
; =>
; "Weight: 420kg"
```

```
(resolve {:content "Hi, my name is %!" :replacements ["John"]})
; =>
; "Hi, my name is John!"
```

```
(resolve {:content "%1 of %2 item(s) downloaded" :replacements [1 5]})
; =>
; "1 of 5 item(s) downloaded"
```

If the content is a number it will be converted to a string and the `:prefix`
and `:suffix` properties can be used.

```
(resolve {:content 420})
; =>
; "420"
```

```
(resolve {:content 420 :prefix "Weight: " :suffix "kg"})
; =>
; "Weight: 420kg"
```

If the content is a keyword it will be evaluated as a multilingual term ID by using
the [`cljc-app-dictionary`](https://github.com/bithandshake/cljc-app-dictionary) library.

```
(ns my-namespace
    (:require [app-dictionary.api :as app-dictionary]
              [metamorphic-content.api :refer [resolve]]))

(app-dictionary/add-term! :apple {:en "Apple" :hu "Alma"})              
(app-dictionary/select-language! :en)

(resolve {:content :apple})
; =>
; "Apple"
```

Dictionary terms also can be used with the `:prefix`, `:suffix` and the `:replacements`
properties.

```
(ns my-namespace
    (:require [app-dictionary.api :as app-dictionary]
              [metamorphic-content.api :refer [resolve]]))

(app-dictionary/add-term! :hi-my-name-is-n {:en "Hi, my name is %!" :hu "Szia, az én nevem %!"})              
(dictionary/select-language! :en)

(resolve {:content :hi-my-name-is-n :replacements ["John"]})
; =>
; "Hi, my name is John!"
```

### Metamorphic content as a Reagent component

You can use the [`metamorphic-content.api/resolve`](documentation/cljc/metamorphic-content/API.md#resolve)
function as a Reagent component as well and it can also resolve contents like Hiccup structures,
Reagent components and React forms.

If the content is a Hiccup structure the component shows it as it is and no additional
property can be used.

```
[resolve {:content [:div "Apple"]}]
```

If the content is a Reagent component you can use the `:params` property.

```
(defn my-component []
  [:div "Apple"])

[resolve {:content [my-component]}]
```

```
(defn my-component [fruit]
  [:div fruit])

[resolve {:content [my-component "Apple"]}]
```

```
(defn my-component [fruit]
  [:div fruit])

[resolve {:content [my-component]
          :params ["Apple"]}]
```

If the content is a symbol of a Reagent component you can use the `:params` property.

```
(defn my-component []
  [:div "Apple"])

[resolve {:content #'my-component}]
```

```
(defn my-component [fruit]
  [:div fruit])

[resolve {:content #'my-component
          :params ["Apple"}]
```
