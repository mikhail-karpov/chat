export function formatDate(date: Date) {

  const today = new Date();

  if (date.getDate() === today.getDate() &&
      date.getMonth() === today.getMonth() &&
      date.getFullYear() === today.getFullYear()) {

    const todayFormatter = new Intl.DateTimeFormat("en-UK", {
      hour: "2-digit",
      minute: "2-digit"
    });
    return todayFormatter.format(date);
  }

  const dateFormatter = new Intl.DateTimeFormat("en-UK",{
    day: "2-digit",
    month: "2-digit",
    year: "numeric"
  });
  return dateFormatter.format(date);
}
