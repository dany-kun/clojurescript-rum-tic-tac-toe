(ns tictac.core
  (:require [tictac.component :as component]
            [rum.core :as rum]
            [tictac.setup :as setup]))

(enable-console-print!)

(defn render! [state]
  (component/mount state))

(render! setup/state)