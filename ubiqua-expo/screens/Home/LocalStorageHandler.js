// export const updateMoneyOnLocalStorage = () => {
//   setLocalStorageData("@money", JSON.stringify({ money: money }))
//     .then(res => {
//       console.log("salvou money?", res);
//     })
//     .catch(err => console.log(err));
// };

// export const updateBillsOnLocalStorage = () => {
//   setLocalStorageData("@bills", JSON.stringify({ bills: bills }))
//     .then(res => {
//       console.log("salvou bill?", res);
//     })
//     .catch(err => console.log(err));
// };

import { AsyncStorage } from "react-native";

export const LocalTypeKeys = {
  MONEY: "@payYourBills/money",
  BILLS: "@payYourBills/bills"
};

export const setLocalStorageData = (storage_key, value) => {
  return new Promise(async (resolve, reject) => {
    try {
      await AsyncStorage.setItem(storage_key, JSON.stringify(value));

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
        resolve(JSON.parse(value));
      }
    } catch (error) {
      // Error retrieving data
      reject(error);
    }
  });
};
