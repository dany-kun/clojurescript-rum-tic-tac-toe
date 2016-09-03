(ns tictac.board.events
  (:require [cljs.reader :as reader]
            [tictac.board.redraw :as r]
            [tictac.init :as setup]))


(defn can-move-piece [{:keys [board player]}]
  (let [all-places (flatten board)]
    (= setup/side (count (filter #(= % player) all-places)))))

(defn store-drag-position [[r c]]
  #(.setData (.-dataTransfer %) "text" (str [r c])))

(defn empty-spot? [board coord]
  (= :empty (get-in board coord)))

(def retrieve-drag-position
  #(reader/read-string (.getData (.-dataTransfer %) "text")))

(defn drop-allowed? [board src target]
  (and (empty-spot? board target)
       (every? #(< (js/Math.abs %) 2) (map - src target))))

(defn on-element-drop [{:keys [board] :as state} coord]
  (fn [e]
    (let [src (retrieve-drag-position e)
          drop-allowed (drop-allowed? board src coord)]
      (when drop-allowed
        (r/update-board-state state coord src)))))

(defn action-allowed? []
  (not (deref setup/game-over)))

(defn fill-box [{:keys [board] :as state} coord]
  (when (and (action-allowed?) (empty-spot? board coord))
    (if (can-move-piece state)
      {:on-drop (on-element-drop state coord)}
      {:on-click #(r/update-board-state state coord)})))

(defn clear-box [{:keys [board player] :as state} coord]
  (when (and (action-allowed?) (can-move-piece state) (= player (get-in board coord)))
    {:on-drag-start (store-drag-position coord) :draggable true}))
