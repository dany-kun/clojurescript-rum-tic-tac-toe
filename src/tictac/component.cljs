(ns tictac.component
  (:require [rum.core :as rum]
            [tictac.events :as event]
            [tictac.setup :as setup]))

(declare mount)

(def container (delay (js/document.getElementById "board-container")))

(rum/defc box
  < {:key-fn (fn [_ _ i j] (str "col-" j "row-" i))}
  [state row-idx col-idx piece]
  (let [action (event/box-actions state row-idx col-idx mount)]
    [:div
     (merge {:class "col" :on-drag-over #(.preventDefault %)} action)
     (piece setup/pieces)]))


(rum/defc row
  < {:key-fn (fn [_ i _] (str "row-" i))}
  [state row-index row]
  [:div {:class "row"} (map-indexed (partial box state row-index) row)])

(rum/defc draw-board [{:keys [board player] :as state}]
  [:div (map-indexed (partial row (assoc state :player player)) board)])

(defn mount [state]
  (rum/mount (draw-board state) @container))