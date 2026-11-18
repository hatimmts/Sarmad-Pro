(() => {
  "use strict";
  const state = { front: null, back: null };
  const storageKey = "sarmad-specimen-project-v1";
  const safeSvg = (side) => {
    const accent = side === "front" ? "#38bdf8" : "#a78bfa";
    const label = side === "front" ? "SIDE A" : "SIDE B";
    return `data:image/svg+xml;charset=utf-8,${encodeURIComponent(`<svg xmlns="http://www.w3.org/2000/svg" width="900" height="520" viewBox="0 0 900 520"><rect width="900" height="520" rx="24" fill="#172554"/><rect x="24" y="24" width="852" height="472" rx="16" fill="none" stroke="${accent}" stroke-width="5"/><circle cx="180" cy="260" r="90" fill="none" stroke="${accent}" stroke-width="8"/><path d="M110 260h140M180 190v140" stroke="${accent}" stroke-width="5"/><text x="450" y="235" fill="white" font-family="sans-serif" font-size="62" font-weight="700" text-anchor="middle">SYNTHETIC</text><text x="450" y="310" fill="${accent}" font-family="sans-serif" font-size="42" text-anchor="middle">${label} · ARCHIVE STUDY</text><text x="450" y="410" fill="#bfdbfe" font-family="sans-serif" font-size="24" text-anchor="middle">NON-NEGOTIABLE SAMPLE ASSET</text></svg>`)}`;
  };
  const preview = side => document.querySelector(`#${side}Preview`);
  const render = side => {
    const data = state[side];
    const el = preview(side);
    if (!data) { el.innerHTML = '<div class="empty">Generate a safe sample or choose an archival photo.</div>'; return; }
    el.innerHTML = `<img alt="${side === "front" ? "Side A" : "Side B"} specimen preview" src="${data.src}">`;
    const image = el.querySelector("img");
    const crop = Number(document.querySelector(`.crop[data-side="${side}"]`).value);
    const perspective = Number(document.querySelector(`.perspective[data-side="${side}"]`).value);
    const brightness = Number(document.querySelector(`.brightness[data-side="${side}"]`).value);
    image.style.transform = `scale(${1 + crop / 100}) rotate(${perspective / 3}deg)`;
    image.style.filter = `brightness(${brightness}%)`;
    document.querySelector(`[data-output="crop-${side}"]`).textContent = `${crop}%`;
    document.querySelector(`[data-output="perspective-${side}"]`).textContent = `${perspective}°`;
    document.querySelector(`[data-output="brightness-${side}"]`).textContent = `${brightness}%`;
  };
  document.querySelectorAll(".sample").forEach(button => button.addEventListener("click", () => {
    const side = button.dataset.side;
    state[side] = { src: safeSvg(side), source: "generated synthetic sample" };
    render(side);
  }));
  document.querySelectorAll(".upload").forEach(input => input.addEventListener("change", () => {
    const file = input.files[0];
    if (!file || !file.type.startsWith("image/")) return;
    if (!/(specimen|sample|archive|non[-_ ]?negotiable)/i.test(file.name)) {
      input.value = "";
      window.alert("For safety, choose an image whose filename identifies it as a specimen, sample, archive, or non-negotiable asset.");
      return;
    }
    const reader = new FileReader();
    reader.onload = () => { state[input.dataset.side] = { src: reader.result, source: "local archival image" }; render(input.dataset.side); };
    reader.readAsDataURL(file);
  }));
  document.querySelectorAll(".controls input").forEach(control => control.addEventListener("input", () => render(control.dataset.side)));
  document.querySelector("#runOcr").addEventListener("click", () => {
    document.querySelector("#ocrText").value = "Synthetic archival study — geometric blue field, printed sample asset; no monetary or identifying marks.";
  });
  document.querySelector("#saveProject").addEventListener("click", () => {
    const payload = { name: document.querySelector("#projectName").value.trim() || "Untitled specimen", ocr: document.querySelector("#ocrText").value, sides: state };
    try {
      localStorage.setItem(storageKey, JSON.stringify(payload));
      document.querySelector("#saveStatus").textContent = "Saved locally";
    } catch { document.querySelector("#saveStatus").textContent = "Storage full; use smaller images."; }
  });
  try {
    const saved = JSON.parse(localStorage.getItem(storageKey) || "null");
    if (saved) {
      document.querySelector("#projectName").value = saved.name || "Untitled specimen";
      document.querySelector("#ocrText").value = saved.ocr || "";
      ["front", "back"].forEach(side => { if (saved.sides && saved.sides[side]) { state[side] = saved.sides[side]; render(side); } });
    }
  } catch { /* Ignore malformed local-only project data. */ }
})();
