(defproject tictac "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [rum "0.10.5"]]
  :clean-targets ^{:protect false} [:target-path "out" "resources/public/cljs"] ;; Add "resources/public/cljs"
  :plugins [[lein-cljsbuild "1.1.1"]]
  :cljsbuild {
              :builds [{:id           "dev"
                        :source-paths ["src" "script"]
                        :figwheel     true
                        :compiler     {:main       "tictac.core"
                                       :asset-path "cljs/out"
                                       :output-to  "resources/public/cljs/main.js"
                                       :output-dir "resources/public/cljs/out"}
                        }
                       {:id           "min"
                        :source-paths ["src"]
                        :compiler     {:output-to     "resources/public/cljs/main.js"
                                       :main          "tictac.core"
                                       :optimizations :advanced
                                       :pretty-print  false}}]
              }
  :figwheel {
             :css-dirs ["resources/public/css"]
             }
  :profiles {
             :dev {:dependencies [[figwheel-sidecar "0.5.0"]]}
             }
  )
