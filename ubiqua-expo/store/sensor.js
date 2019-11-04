import { createStore } from "redux";
import { combineReducers } from "redux";

const INITIAL_STATE = [
  { title: "Air Conditioning", id: 0, environment: "Room 1" },
  { title: "Philips Lamp", id: 0, environment: "Room 1" },
  { title: "Door", id: 0, environment: "Room 2" }
];

const sensorsActionTypes = {
  UPDATE: "UPDATE",
  UPDATE_ALL: "UPDATE_ALL",
  DELETE: "DELETE",
  ADD: "ADD"
};

// ACTIONS

export const actionUpdateAllSensors = sensors => {
  return { type: sensorsActionTypes.UPDATE_ALL, payload: { sensors: sensors } };
};

// actions for a single sensor

export const actionUpdateSensor = (sensor, id) => {
  return {
    type: sensorsActionTypes.UPDATE,
    payload: { sensor: sensor, id: id }
  };
};
export const actionDeleteSensor = id => ({
  type: sensorsActionTypes.DELETE,
  payload: { id: id }
});
export const actionAddSensor = sensor => {
  return {
    type: sensorsActionTypes.ADD,
    payload: { sensor: sensor }
  };
};

// REDUCER

export const sensorReducer = (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case sensorsActionTypes.UPDATE:
      return state.map(sensor =>
        sensor.id === action.payload.id ? action.payload.sensor : sensor
      );
    case sensorsActionTypes.DELETE:
      return state.filter(sensor => sensor.id !== action.payload.id);

    case sensorsActionTypes.ADD:
      return [
        ...state,
        {
          id: action.payload.sensor.id,
          environment: action.payload.sensor.environment,
          title: action.payload.sensor.title
        }
      ];
    case sensorsActionTypes.UPDATE_ALL:
      return action.payload.sensors;
    default:
      return state;
  }
};

export const Reducers = combineReducers({
  sensorsState: sensorReducer
});

export const Store = createStore(Reducers);
