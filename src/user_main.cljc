(ns user-main
  (:require contrib.ednish
            [hyperfiddle.api :as hf]
            [hyperfiddle.electric :as e]
            [hyperfiddle.electric-dom2 :as dom]
            ;router librarysi -->
            [hyperfiddle.history :as history]

            app.demo-index

            ))



(e/defn NotFoundPage []
        (e/client (dom/h1 (dom/text "Page not found!"))))

;router kullanmak istediğimiz butün pageleri buraya eklememiz gerekiyor ----->
(e/defn Pages [page]
        (e/server
          (case page
            `app.demo-index/Demos app.demo-index/Demos
            NotFoundPage)))




;;bu method routing islemleri ile ilgilenen method ---->
(e/defn Main []
        (binding [history/encode contrib.ednish/encode-uri
                  history/decode #(or (contrib.ednish/decode-path % hf/read-edn-str)
                                      [`app.demo-index/Demos])]
          (history/router (history/HTML5-History.)
                          (set! (.-title js/document) (str (clojure.string/capitalize (name (first history/route))) " - Hyperfiddle"))
                          (binding [dom/node js/document.body])
                          (dom/pre (dom/text (contrib.str/pprint-str history/route)))
                          (let [[page & args] history/route]
                            (e/server (new (Pages. page #_args)))))))