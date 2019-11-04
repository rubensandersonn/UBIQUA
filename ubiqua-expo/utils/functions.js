import { ToastAndroid } from "react-native";

export const toast = msg => {
  ToastAndroid.showWithGravityAndOffset(
    msg,
    ToastAndroid.SHORT,
    ToastAndroid.BOTTOM,
    25,
    50
  );
};
