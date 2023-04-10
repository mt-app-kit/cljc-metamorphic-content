
# cljc-metamorphic-content

### Overview

The <strong>cljc-metamorphic-content</strong> is a simple Clojure/ClojureScript
contents renderer that you can pass different types of content such as Reagent
components, React forms, Hiccup structures, numbers, strings or multilingual terms.

### deps.edn

```
{:deps {bithandshake/cljc-metamorphic-content {:git/url "https://github.com/bithandshake/cljc-metamorphic-content"
                                               :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}}
```

### Current version

Check out the latest commit on the [release branch](https://github.com/bithandshake/cljc-metamorphic-content/tree/release).

### Documentation

The <strong>cljc-metamorphic-content</strong> functional documentation is [available here](documentation/COVER.md).

### Changelog

You can track the changes of the <strong>cljc-metamorphic-content</strong> library [here](CHANGES.md).

### Index

- [Metamorphic content as a function](#metamorphic-content-as-a-function)
- [Metamorphic content as a Reagent component](#metamorphic-content-as-a-reagent-component)

# Usage

### Metamorphic content as a function

The [`metamorphic-content.api/value`](documentation/cljc/metamorphic-content/API.md#value)
function takes a map (or its shorthand form) that describes the content what the function returns.

If the content has no additional properties the shorthand form could be used as well.

```
(value {:content "Apple"})
; => "Apple"

(value "Apple")
; => "Apple"
```

If the content is a string you can pass the `:prefix` and `:suffix` properties which
will be joined to the content and/or the `:replacements` property as a vector that
contains replacement values.

```
(value {:content "Apple"})
; => "Apple"
```

```
(value {:content "420" :prefix "Weight: " :suffix "kg"})
; => "Weight: 420kg"
```

```
(value {:content "Hi, my name is %!" :replacements ["John"]})
; => "Hi, my name is John!"
```

```
(value {:content "%1 of %2 item(s) downloaded" :replacements [1 5]})
; => "1 of 5 item(s) downloaded"
```

If the content is a number it will be converted to a string and the `:prefix`
and `:suffix` properties can be used.

```
(value {:content 420})
; => "420"
```

```
(value {:content 420 :prefix "Weight: " :suffix "kg"})
; => "Weight: 420kg"
```

If the content is a keyword it will be evaluated as a multilingual term ID by using
the [`cljc-dictionary`](https://github.com/bithandshake/cljc-dictionary) library.

```
(ns my-namespace
    (:require [dictionary.api :as dictionary]
              [metamorphic-content.api :refer [value]]))

(dictionary/add-term! :apple {:en "Apple" :hu "Alma"})              
(dictionary/select-language! :en)

(value {:content :apple})
; => "Apple"
```

Dictionary terms also can be used with the `:prefix`, `:suffix` and the `:replacements`
properties.

```
(ns my-namespace
    (:require [dictionary.api :as dictionary]
              [metamorphic-content.api :refer [value]]))

(dictionary/add-term! :hi-my-name-is-n {:en "Hi, my name is %!" :hu "Szia, az én nevem %!"})              
(dictionary/select-language! :en)

(value {:content :hi-my-name-is-n :replacements ["John"]})
; => "Hi, my name is John!"
```

### Metamorphic content as a Reagent component

The [`metamorphic-content.api/component`](documentation/cljc/metamorphic-content/API.md#component)
function is a Reagent component that takes a map (or its shorthand form) that describes its content.

Like the [`metamorphic-content.api/value`](documentation/cljc/metamorphic-content/API.md#value)
function it can display numbers, strings and multilingual terms as its content
but as a Reagent component it can handle Hiccups, other Reagent components and React forms.

It can take the content in shorthand form as well as the `value` function below.

If the content is a Hiccup structure the component shows it as it is and no additional
property can be used.

```
[component {:content [:div "Apple"]}]
```

If the content is a Reagent component you can use the `:params` property.

```
(defn my-component []
  [:div "Apple"])

[component {:content [my-component]}]
```

```
(defn my-component [fruit]
  [:div fruit])

[component {:content [my-component "Apple"]}]
```

```
(defn my-component [fruit]
  [:div fruit])

[component {:content [my-component]
            :params ["Apple"]}]
```

If the content is a symbol of a Reagent component you can use the `:params` property.

```
(defn my-component []
  [:div "Apple"])

[component {:content #'my-component}]
```

```
(defn my-component [fruit]
  [:div fruit])
  
[component {:content #'my-component
            :params ["Apple"}]
```
