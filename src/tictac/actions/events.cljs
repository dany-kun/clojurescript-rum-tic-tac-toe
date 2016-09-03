(ns tictac.events
  (:require [cljs.reader :as reader]
            [tictac.actions.redraw :as r]
            [tictac.setup :as setup]))


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

(defn on-element-drop [{:keys [board] :as state} coord render]
  (fn [e]
    (let [src (retrieve-drag-position e)
          drop-allowed (drop-allowed? board src coord)]
      (when drop-allowed
        (render (r/update-board state coord src))))))

(defn on-box-clicked [{:keys [board] :as state} coord render]
  #(when (empty-spot? board coord)
    (render (r/update-board state coord))))

(defn box-action [{:keys [board player] :as state} board-full? coord render]
  (if board-full?
    (cond
      (empty-spot? board coord) {:on-drop (on-element-drop state coord render)}
      (= player (get-in board coord)) {:on-drag-start (store-drag-position coord) :draggable true})
    {:on-click (on-box-clicked state coord render)}))

(defn box-actions [state row-idx col-idx render]
  (let [coord [row-idx col-idx]
        board-full? (can-move-piece state)]
    (box-action state board-full? coord render)))
