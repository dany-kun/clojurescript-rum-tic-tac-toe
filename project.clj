(defproject tictac "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [figwheel-sidecar "0.5.0"]
                 [rum "0.10.5"]]
  :plugins [[lein-cljsbuild "1.1.0"]]
  :clean-targets ^{:protect false} [:target-path "out" "resources/public/cljs"] ;; Add "resources/public/cljs"
  :cljsbuild {
              :builds [{:id           "dev"
                        :source-paths ["src" "script"]
                        :figwheel     true
                        :compiler     {:main       "tictac.core"
                                       ;; add the following
                                       :asset-path "cljs/out"
                                       :output-to  "resources/public/cljs/main.js"
                                       :output-dir "resources/public/cljs/out"}
                        }]
              }
  :figwheel {
             :css-dirs ["resources/public/css"]
             }
  )
