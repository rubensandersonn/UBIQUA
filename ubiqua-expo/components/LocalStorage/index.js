import { AsyncStorage } from "react-native";

export const setLocalStorageData = (storage_key, value) => {
  return new Promise(async (resolve, reject) => {
    try {
      await AsyncStorage.setItem(storage_key, value);
      console.log("didit?");
      resolve(true);
    } catch (error) {
      // Error saving data
      reject(error);
    }
  });
};

export const getLocalStorageData = storage_key => {
  return new Promise(async (resolve, reject) => {
    try {
      const value = await AsyncStorage.getItem(storage_key);
      if (value !== null) {
        // We have data!!
        resolve(value);
      }
    } catch (error) {
      // Error retrieving data
      reject(error);
    }
  });
};
